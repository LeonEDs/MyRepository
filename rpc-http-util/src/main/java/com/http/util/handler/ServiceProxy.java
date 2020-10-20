package com.http.util.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.http.util.annotation.RequestHeadConfig;
import com.http.util.annotation.Param;
import com.http.util.annotation.RequestPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态代理，需要注意的是，这里用到的是JDK自带的动态代理，代理对象只能是接口，不能是类
 */
@Slf4j
public class ServiceProxy<T> implements InvocationHandler
{

    private Class<T> interfaceType;

    public ServiceProxy(Class<T> interfaceType)
    {
        this.interfaceType = interfaceType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if (Object.class.equals(method.getDeclaringClass()))
        {
            return method.invoke(this, args);
        }

        boolean flag = method.isAnnotationPresent(RequestPath.class);

        if (flag)
        {
            //获取方法上的自定义注解
            RequestPath annotationPath = method.getAnnotation(RequestPath.class);
            RequestHeadConfig annotationHead = method.getAnnotation(RequestHeadConfig.class);

            //请求配置
            StringBuilder urlBuilder = new StringBuilder(annotationPath.url());
            urlBuilder.append("?");
            HttpMethod requestMethod = annotationPath.method();

            //请求头配置
            String[] headers = null;
            if (annotationHead != null)
            {
                headers = annotationHead.header();
            }

            //请求参数类型, GET请求默认参数拼接URI ， POST请求默认参数转BODY-JSON
            boolean isGet = requestMethod.matches(HttpMethod.GET.name());
            String defaultParamType = isGet ? Param.URI : Param.JSON;

            //请求处理器
            Class<? extends IRequestHandler> requestClass = annotationPath.requestHandler();
            IRequestHandler requestHandler = requestClass.newInstance();
            Parameter[] methodParams = method.getParameters();
            String param = null;
            boolean isEmptyBody = true;

            for (int i = 0; i < args.length; i++)
            {
                Parameter methodParam = methodParams[i];
                Param methodParamAnnotation = methodParam.getAnnotation(Param.class);
                String name;
                String type;
                if (methodParamAnnotation != null)
                {
                    name = StringUtils.isEmpty(methodParamAnnotation.name()) ? null : methodParamAnnotation.name();
                    type = StringUtils.isEmpty(methodParamAnnotation.type()) ? defaultParamType : methodParamAnnotation.type();
                }else
                {
                    throw new RuntimeException("parameter does not declare @Annotation: com.http.util.annotation.Param");
                }
                if (Param.URI.equals(type))
                {
                    urlBuilder.append(name).append("=").append(args[i]).append("&");
                }
                if (Param.JSON.equals(type))
                {
                    if (isEmptyBody)
                    {
                        param = JSONObject.toJSONString(args[i]);
                        isEmptyBody = false;
                    }else //Http Body请求参数不能多个配置，请整合成一个对象
                    {
                        throw new RuntimeException("JSON RAW cant cover more request parameter");
                    }
                }
            }


            String url = urlBuilder.toString();
            if (url.endsWith("&") || url.endsWith("?"))
            {
                url = url.substring(0, url.length() - 1);
            }
            String result = requestHandler.send(url, requestMethod, param, coverHeaders(headers));

            // 处理返回值
            Class<?> returnType = method.getReturnType();
            Object resultClazz = JSONObject.parseObject(result, returnType);
            return returnType.cast(resultClazz);
        } else
        {
            log.warn("parameter does not declare @Annotation: com.http.util.annotation.RequestPath");
        }

        //这里可以得到参数数组和方法等，可以通过反射，注解等，进行结果集的处理
        //mybatis就是在这里获取参数和相关注解，然后根据返回值类型，进行结果集的转换
        return null;
    }

    public static Header[] coverHeaders(String[] args)
    {
        List<Header> list = new ArrayList<>();
        if (args != null && args.length > 0)
        {
            for (String arg : args)
            {
                String[] keyValue = arg.split(":");
                if (keyValue.length >= 2)
                {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    Header header = new BasicHeader(key, value);
                    list.add(header);
                }
            }
        }
        Header[] result = new Header[list.size()];
        list.toArray(result);
        return result;
    }
}