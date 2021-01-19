package com.xad.server.proxy.handler;

import com.xad.server.proxy.annotation.RestGet;
import com.xad.server.proxy.annotation.RestParam;
import com.xad.server.proxy.annotation.RestPost;
import com.xad.server.proxy.annotation.RestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态代理，需要注意的是，这里用到的是JDK自带的动态代理，代理对象只能是接口，不能是类
 */
@Slf4j
@Component
public class ServiceProxy implements InvocationHandler
{
    RestTemplate restTemplate;

    private Class<?> interfaceType;


    public void setRestTemplate(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public void setInterfaceType(Class<?> interfaceType)
    {
        this.interfaceType = interfaceType;
    }

    private static final ServiceProxy instance = new ServiceProxy();
    private ServiceProxy() {}

    public static ServiceProxy getInstance()
    {
        return instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if (Object.class.equals(method.getDeclaringClass()))
        {
            return method.invoke(this, args);
        }

        boolean flag = interfaceType.isAnnotationPresent(RestRequest.class);

        if (flag)
        {
            try
            {
                //获取自定义注解
                RestRequest httpRequest = interfaceType.getAnnotation(RestRequest.class);
                RestGet httpGet = method.getAnnotation(RestGet.class);
                RestPost httpPost = method.getAnnotation(RestPost.class);
                Parameter[] methodParams = method.getParameters();
                Class<?> returnType = method.getReturnType();

                //请求配置
                String url = "";
                if (!StringUtils.isEmpty(httpRequest.path()))
                {
                    url += httpRequest.path();
                }


                if (httpGet != null)
                {
                    url += httpGet.path();
                    url += "?";
                    Map<String, Object> mapParam = new HashMap<>();
                    if (args != null && args.length > 0)
                    {
                        for (int i = 0; i < methodParams.length; i++)
                        {
                            Parameter methodParam = methodParams[i];
                            RestParam paramAnnotation = methodParam.getAnnotation(RestParam.class);
                            if (paramAnnotation != null)
                            {
                                String paramName = paramAnnotation.value();
                                url += paramName + "={" + paramName + "}&";
                                mapParam.put(paramName, args[i]);
                            }
                        }
                    }
                    if (url.endsWith("&") || url.endsWith("?"))
                    {
                        url = url.substring(0, url.length() - 1);
                    }

                    return restTemplate.getForObject(url, returnType, mapParam);
                }
                if (httpPost != null)
                {
                    url += httpPost.path();
                    Object param = null;
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.APPLICATION_JSON);
                    if (args != null && args.length > 0)
                    {
                        for (int i = 0; i < methodParams.length; i++)
                        {
                            Parameter methodParam = methodParams[i];
                            RestParam paramAnnotation = methodParam.getAnnotation(RestParam.class);
                            if (paramAnnotation != null)
                            {
                                param = args[i];
                            }
                        }
                        if (param == null)
                        {
                            param = args[0];
                        }
                    }

                    return restTemplate.postForObject(url, param, returnType);
                }

                throw new RuntimeException("No Rest Request was send");
            } catch (Exception e)
            {
                log.warn("Error: ", e);
            }

        } else
        {
            log.warn("parameter does not declare @Annotation: " + RestRequest.class.getName());
        }

        //这里可以得到参数数组和方法等，可以通过反射，注解等，进行结果集的处理
        //mybatis就是在这里获取参数和相关注解，然后根据返回值类型，进行结果集的转换
        return null;
    }
}