package com.xad.demo.config;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/18
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan("com.xad.demo")
@MapperScan(basePackages = "com.xad.demo.mapper")
public class SpringConfig
{
    @Bean
    public DataSource setDataSource()
    {
        DataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://10.162.122.72:3306/test-crm-01?useUnicode=true&characterEncoding=utf8");
        properties.setProperty("username", "test-crm");
        properties.setProperty("password", "DispC_crm_3142");
        dataSourceFactory.setProperties(properties);
        return dataSourceFactory.getDataSource();
    }

    @Bean
    public DataSourceTransactionManager setDataSourceTransactionManager(DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory setSqlSessionFactory(DataSource dataSource) throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:/mapper/**/*.xml"));     // 配置xml文件位置
        return sqlSessionFactoryBean.getObject();
    }
}
