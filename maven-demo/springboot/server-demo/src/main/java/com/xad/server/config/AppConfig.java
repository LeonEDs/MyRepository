package com.xad.server.config;


import com.xad.server.proxy.handler.ServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig
{
    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate getRestTemplate()
    {
        RestTemplate template = builder.build();
        ServiceProxy.getInstance().setRestTemplate(template);
        return template;
    }

}
