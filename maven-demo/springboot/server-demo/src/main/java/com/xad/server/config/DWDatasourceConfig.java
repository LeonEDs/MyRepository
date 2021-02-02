package com.xad.server.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * DWS 数据源配置.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Configuration
@MapperScan(basePackages = "com.xad.server.dwmapper", sqlSessionFactoryRef = "dwConfigSqlSessionFactory")
public class DWDatasourceConfig
{
    @Autowired
    @Qualifier("dwDataSource")
    private DataSource dwDataSource;

    @Autowired
    @Qualifier("pagePlugin")
    private PaginationInterceptor pagePlugin;

    @Autowired
    @Qualifier("sqlOutPlugin")
    private PerformanceInterceptor sqlOutPlugin;


    /**
     * sqlSessionFactory.
     */
    @Bean(name = "dwConfigSqlSessionFactory")
    public SqlSessionFactory dwConfigSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dwDataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:/dwmapper/**/*.xml"));
        factoryBean.setPlugins(new Interceptor[]{pagePlugin, sqlOutPlugin});
        return factoryBean.getObject();
    }

    /**
     * transaction manager.
     */
    @Bean(name = "dwConfigTransactionManager")
    public DataSourceTransactionManager dwConfigTransactionManager() {
        return new DataSourceTransactionManager(dwDataSource);
    }

    /**
     * sqlSessionTemplate.
     */
    @Bean(name = "dwConfigSqlSessionTemplate")
    public SqlSessionTemplate dwConfigSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(dwConfigSqlSessionFactory());
    }

    @Bean(name = "dwJdbcTemplate")
    public NamedParameterJdbcTemplate dwJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dwDataSource);
    }
}
