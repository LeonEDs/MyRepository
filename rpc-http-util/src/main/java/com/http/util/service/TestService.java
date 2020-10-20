package com.http.util.service;

import com.http.util.KeyValue;
import com.http.util.annotation.Param;
import com.http.util.annotation.RequestPath;
import com.http.util.annotation.RestHttpClient;
import org.springframework.http.HttpMethod;

@RestHttpClient
public interface TestService
{
    @RequestPath(url = "http://127.0.0.1:8081/xxx/getInfo", method = HttpMethod.GET)
    KeyValue getInfo(@Param(name = "message") String username);

    @RequestPath(url = "http://127.0.0.1:8081/xxx/postInfo", method = HttpMethod.POST)
    KeyValue postInfo(@Param(type = Param.JSON) KeyValue keyValue);
}
