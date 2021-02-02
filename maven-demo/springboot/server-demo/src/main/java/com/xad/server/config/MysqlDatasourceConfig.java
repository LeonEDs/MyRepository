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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Mysql 数据源配置.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Configuration
@MapperScan(basePackages = "com.xad.server.mapper", sqlSessionFactoryRef = "mysqlConfigSqlSessionFactory")
public class MysqlDatasourceConfig
{
    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("pagePlugin")
    private PaginationInterceptor pagePlugin;

    @Autowired
    @Qualifier("sqlOutPlugin")
    private PerformanceInterceptor sqlOutPlugin;

    /**
     * sqlSessionFactory.
     */
    @Bean(name = "mysqlConfigSqlSessionFactory")
    @Primary
    public SqlSessionFactory configSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml"));     // 配置xml文件位置
        factoryBean.setPlugins(new Interceptor[]{pagePlugin, sqlOutPlugin});
        return factoryBean.getObject();
    }

    /**
     * transactionManager.
     */
    @Bean(name = "mysqlConfigTransactionManager")
    @Primary
    public DataSourceTransactionManager configTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * sqlSessionTemplate.
     */
    @Bean(name = "mysqlConfigSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate configSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(configSqlSessionFactory());
    }
}
