
package com.aliyun.tablestore.examples.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.aliyun.tablestore.examples.dao.dla", sqlSessionTemplateRef  = "dlaSqlSessionTemplate")
public class DLADataSourceConfig {


    @Value("${mybatis.mapperLocations}")
    private String locations;


    @Bean(name = "dlaDataSource")
    @ConfigurationProperties(prefix = "spring.dlasource")
    public DataSource testDataSource() {
        DataSource dataSource=DataSourceBuilder.create()
                .type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
        return dataSource;
    }

    @Bean(name = "dlaSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("dlaDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(locations));

        return bean.getObject();
    }

    @Bean(name = "dlaSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("dlaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}