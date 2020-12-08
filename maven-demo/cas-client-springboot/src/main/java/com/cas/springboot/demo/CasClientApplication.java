package com.cas.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jasig.cas.client.boot.configuration.EnableCasClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableCasClient
public class CasClientApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CasClientApplication.class);
        System.out.println("--------------SUCCESS--------------");
    }

    @GetMapping("/getInfo")
    public String getInfo()
    {
        return "Success";
    }
}
