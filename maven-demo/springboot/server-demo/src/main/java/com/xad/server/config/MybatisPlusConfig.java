package com.xad.server.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * mybatis plus 插件.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig
{

    /**
     * 分页插件.
     */
    @Bean("pagePlugin")
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 打印 sql.
     */
    @Bean("sqlOutPlugin")
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor =new PerformanceInterceptor();
        //格式化sql语句
        Properties properties =new Properties();
        properties.setProperty("format", "false");
        performanceInterceptor.setProperties(properties);
        return performanceInterceptor;

    }
}