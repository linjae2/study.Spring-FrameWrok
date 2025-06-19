package org.example.config;

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
}
