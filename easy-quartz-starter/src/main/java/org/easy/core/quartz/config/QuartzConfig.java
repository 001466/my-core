package org.easy.core.quartz.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return dataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.quartz.properties.org.quartz.datasource.quartzdatasource")
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @QuartzDataSource
    @Bean("quartzdatasource")
    public DataSource quartzDataSource() {
        DataSource datasource = quartzDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
        return datasource;
    }


}
