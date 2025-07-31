package org.snudh.psv;

import org.snudh.psv.domain.TestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CommandLine {
  private final TestService testService;

  @Bean
  public CommandLineRunner getRunnerBean1() {
    return args -> {

      // System.out.println("TEST SERVICE.....");

      testService.test();
    };
  }
}
