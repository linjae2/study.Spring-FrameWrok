package org.snudh.psv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class App {
  public static void main(String[] args) {
    //System.out.println(new App().getGreeting());
    SpringApplication.run(App.class, args);
    System.out.print("======================================================");
  }
}
