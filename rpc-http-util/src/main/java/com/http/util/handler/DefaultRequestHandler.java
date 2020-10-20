package com.http.util.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;
import java.nio.charset.StandardCharsets;

@Slf4j
public class DefaultRequestHandler implements IRequestHandler
{

    @Override
    public String send(String url, HttpMethod method, String param, Header... headers)
    {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String response = null;
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try
        {
            if (HttpMethod.POST.matches(method.name()))
            {
                HttpPost request = new HttpPost(url);

                StringEntity entity = new StringEntity(param, StandardCharsets.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                request.setHeaders(headers);

                response = httpClient.execute(request, responseHandler);
                request.releaseConnection();
            }
            if (HttpMethod.GET.matches(method.name()))
            {
                HttpGet request = new HttpGet(url);
                request.setHeaders(headers);
                response = httpClient.execute(request, responseHandler);
                request.releaseConnection();
            }

            return response;
        } catch (Exception e)
        {
            log.error("HttpClient Request ERROR: ", e);
        }

        return null;
    }
}