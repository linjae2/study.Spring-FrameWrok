package org.snudh.jobrunr.config;

import com.zaxxer.hikari.HikariDataSource;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.spring.autoconfigure.JobRunrProperties;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.StorageProviderUtils;
import org.jobrunr.storage.sql.common.SqlStorageProviderFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JobServerConfig {

  @Bean
  @ConfigurationProperties("org.jobrunr.datasource")
  public DataSourceProperties jobRunrDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("org.jobrunr.datasource.hikari")
  DataSource jobRunrDataSource() {
    //return DataSourceBuilder.create().build();
    return jobRunrDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
  }

  @Bean
  public StorageProvider sqlStorageProvider(
          JobMapper jobMapper,
          JobRunrProperties properties
  ) {
    String tablePrefix = properties.getDatabase().getTablePrefix();

    StorageProviderUtils.DatabaseOptions databaseOptions =
            properties.getDatabase().isSkipCreate() ?
                    StorageProviderUtils.DatabaseOptions.SKIP_CREATE : StorageProviderUtils.DatabaseOptions.CREATE;

    StorageProvider storageProvider = SqlStorageProviderFactory.using(jobRunrDataSource(), tablePrefix, databaseOptions);

    storageProvider.setJobMapper(jobMapper);

    return storageProvider;
  }
}
