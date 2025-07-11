package org.study.runner;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunnerConfig {
  final static Logger log = LoggerFactory.getLogger(RunnerConfig.class);

  @Bean
  public CommandLineRunner getTest1Runner() {
    return args -> {
      Thread thread = new Thread(() -> {
        // System.out.println("스레드 시작 " + LocalDateTime.now());
        log.info("스레드 시작");
      });

      log.trace("This is a TRACE message." );
      log.debug("This is a DEBUG message." );
      log.info ("This is an INFO message." );
      log.warn ("This is a WARN message."  );
      log.error("This is an ERROR message.");

      thread.start();
      thread.join();
    };
  }

  // @Bean
  // public CommandLineRunner getTest2Runner() {
  //   return args -> System.out.println("Test CommandLine Runner #2");
  // }

  // @Bean
  // public ApplicationRunner getApp1Runner() {
  //   return args -> System.out.println("Test Applicatin Runner #1");
  // }
}
