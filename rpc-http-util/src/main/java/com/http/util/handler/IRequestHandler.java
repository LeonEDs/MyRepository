package com.http.util.handler;

import com.http.util.KeyValue;
import org.apache.http.Header;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;

public interface IRequestHandler
{
    default String send(String url, HttpMethod method)
    {
        return send(url, method, null, null);
    }

    default String send(String url, HttpMethod method, String param)
    {
        return send(url, method, param, null);
    }

    String send(String url, HttpMethod method, String param, Header... headers);

}
