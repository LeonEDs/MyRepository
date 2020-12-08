package com.http.util.service;

import com.http.util.proxy.annotation.RestGet;
import com.http.util.proxy.annotation.RestParam;
import com.http.util.proxy.annotation.RestPost;
import com.http.util.proxy.annotation.RestRequest;

import java.util.Map;

@RestRequest(path = "http://127.0.0.1:8181/xxx")
public interface TestService
{
    @RestGet(path = "/getInfo")
    Map<String, Object> getInfo(@RestParam("message") String username);

    @RestPost(path = "/postInfo")
    Map<String, Object> postInfo(@RestParam Map<String, Object> keyValue);
}
