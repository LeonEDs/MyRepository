package com.http.util.rest;

import com.http.util.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller
{
    @Autowired
    TestService testService;

    @GetMapping("test1")
    public void testService1()
    {
        Map<String, Object> getResult = testService.getInfo("AAABBBXXX");
        System.out.println(">>> GET   :   " + getResult.toString());
    }

    @GetMapping("test2")
    public void testService2()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("AAAA","BBBB");
        Map<String, Object> postResult = testService.postInfo(map);
        System.out.println(">>> POST   :   " + postResult.toString());
    }
}
