package org.snudh;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestClient;

@Configuration
public class CommandLine {

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
    return args -> {
      System.out.println("Runner Bean #1");  // 1
    };
  }

  @Bean
  public CommandLineRunner getRunnerBean2() {
    return args -> {
      System.out.println("Runner Bean #2");  // 2
    };
  }

  @Bean
  @Order(1)
  public ApplicationRunner getRunnerBean3() {
    return args -> {
      System.out.println("Runner Bean #3");  // 3
    };
  }
}
