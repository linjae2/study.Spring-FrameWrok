package com.example.jobrunr.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class H2InitDemoApplication {
  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(H2InitDemoApplication.class, args);


  }
}
