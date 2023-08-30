package com.ruyuan.eshop.fulfill.saga;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Configuration
public class DataSourceConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("druidDataSource") DruidDataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }

}
