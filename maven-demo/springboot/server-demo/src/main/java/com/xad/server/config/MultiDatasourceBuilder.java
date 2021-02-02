package com.xad.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 双数据源配置.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Configuration
public class MultiDatasourceBuilder
{
    /**
     * mysql datasource.
     */
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysqldb")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * dws datasource.
     */
    @Bean(name = "dwDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dwdb")
    public DataSource dwDataSource() {
        return DataSourceBuilder.create().build();
    }
}
