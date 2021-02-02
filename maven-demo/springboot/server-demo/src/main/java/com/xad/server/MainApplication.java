package com.xad.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@MapperScan("com.xad.server.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xad.server.service"})
public class MainApplication
{
    public static void main(String [] args)
    {
        SpringApplication.run(MainApplication.class, args);
    }
}
