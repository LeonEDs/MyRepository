package com.http.util.rest;

import com.http.util.KeyValue;
import com.http.util.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class Controller
{
    @Autowired
    TestService testService;

    @GetMapping("/xxx/getInfo")
    public KeyValue getInfo(String message, HttpServletRequest request, HttpServletResponse response)
    {
        return new KeyValue("XXX", message);
    }

    @PostMapping("/xxx/postInfo")
    public KeyValue postInfo(@RequestBody KeyValue message, HttpServletRequest request, HttpServletResponse response)
    {
        return message;
    }
}
