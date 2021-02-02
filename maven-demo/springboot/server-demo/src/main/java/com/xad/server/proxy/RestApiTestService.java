package com.xad.server.proxy;

import com.xad.server.proxy.annotation.RestGet;
import com.xad.server.proxy.annotation.RestParam;
import com.xad.server.proxy.annotation.RestPost;
import com.xad.server.proxy.annotation.RestRequest;

import java.util.Map;

@RestRequest(path = "http://127.0.0.1:8181/xxx")
public interface RestApiTestService
{
    @RestGet(path = "/getInfo")
    Map<String, Object> getInfo(@RestParam("message") String username);

    @RestPost(path = "/postInfo")
    Map<String, Object> postInfo(@RestParam Map<String, Object> keyValue);
}
