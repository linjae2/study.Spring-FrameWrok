package org.example;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunnerConfig {

  @Bean
  public CommandLineRunner getTest1Runner() {
    return args -> System.out.println("Test CommandLine Runner #1");
  }

  @Bean
  public CommandLineRunner getTest2Runner() {
    return args -> System.out.println("Test CommandLine Runner #2");
  }

  @Bean
  public ApplicationRunner getApp1Runner() {
    return args -> System.out.println("Test Applicatin Runner #1");
  }
}
