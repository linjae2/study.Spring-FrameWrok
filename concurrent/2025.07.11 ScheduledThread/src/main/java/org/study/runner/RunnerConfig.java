package org.study.runner;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(8);

    return args -> {
      // 수행시긴이 긴 task (약 3초)
      Runnable task1 = () -> {
        log.info("start");
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();;
        }
        log.info("finish");
      };


      Thread thread = new Thread(() -> {
        log.info("스레드 시작");

        // 0.1 초마다 한번씩 task를 수행시켜주길 바라고 만듦
        executor.scheduleAtFixedRate(task1, 0, 100, TimeUnit.MILLISECONDS);
      });

      thread.start();
      thread.join();
      log.info("프로그램 종료!");
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
