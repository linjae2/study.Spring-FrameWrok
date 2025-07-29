package org.snudh.DataConfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.snudh.mediinfo",
        entityManagerFactoryRef ="MediInfoEntityManagerFactory",
        transactionManagerRef = "MediInfoTransactionManager"
)
public class MediInfoRepositoriesConfig {
  private final JpaProperties properties;
  private final JtaTransactionManager jtaTransactionManager;

  public MediInfoRepositoriesConfig(
          JpaProperties properties,
          ObjectProvider<JtaTransactionManager> jtaTransactionManager
  ) {
    this.properties = properties;
    this.jtaTransactionManager = jtaTransactionManager.getIfAvailable();
  }

  @Bean
  @ConfigurationProperties("spring.mediinfo.datasource")
  public DataSourceProperties mediInfoDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.mediinfo.datasource.hikari")
  public DataSource mediInfoDataSource() {
    return mediInfoDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
  }

  private JpaVendorAdapter jpaVendorAdapter() {
    AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setShowSql(this.properties.isShowSql());
    if (this.properties.getDatabase() != null) {
      adapter.setDatabase(this.properties.getDatabase());
    }
    if (this.properties.getDatabasePlatform() != null) {
      adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
    }
    adapter.setGenerateDdl(this.properties.isGenerateDdl());
    return adapter;
  }

  @Bean
  @Qualifier("MediInfoEntityManager")
  public EntityManagerFactoryBuilder MediInfoEntityManagerFactoryBuilder(
          ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
          ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers
  ) {
    EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter(),
            this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
    customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
    return builder;
  }

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean MediInfoEntityManagerFactory(
          @Qualifier("MediInfoEntityManager")
          EntityManagerFactoryBuilder factoryBuilder
  ) {
    return factoryBuilder.dataSource(mediInfoDataSource())
            //.managedTypes(persistenceManagedTypes)
            .packages("org.snudh.mediinfo")
            //.properties(hibernatePropertyMap)
            .persistenceUnit("MediInfoPersistence")
            //.packages(RepositoryConfig.BASE_PACKAGE)
            .build();
  }

  @Bean
  public PlatformTransactionManager MediInfoTransactionManager(
          ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManagerCustomizers
            .ifAvailable((customizers) -> customizers.customize((TransactionManager) transactionManager));
    return transactionManager;
  }
}
