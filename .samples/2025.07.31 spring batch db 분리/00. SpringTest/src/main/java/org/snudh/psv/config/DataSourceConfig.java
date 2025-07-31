package org.snudh.psv.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
  @Bean @Primary
  @ConfigurationProperties("spring.datasource")
  public DataSourceProperties primaryDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("spring.batch.datasource")
  public DataSourceProperties batchDataSourceProperties() {
      return new DataSourceProperties();
  }

  @Bean @Primary
  public DataSource primaryDataSource() {
    return primaryDataSourceProperties()
      .initializeDataSourceBuilder()
      .build();
  }

  @Bean
  @BatchDataSource
  public DataSource batchDataSource() {
    return batchDataSourceProperties()
      .initializeDataSourceBuilder()
      .build();
  }
}
