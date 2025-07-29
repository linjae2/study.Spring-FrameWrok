package org.snudh;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

import org.snudh.service.TestService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class CommandLine {

  //private final RestClient restClient;
  private final TestService service;

  //@Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
    return args -> {
      System.out.println("Runner Bean #1");  // 2
    };
  }

  public record TestBDto(LocalDate rsvDay, Integer dday, String msgType, String msgData) {}

  //@Bean
  public CommandLineRunner getRunnerBean2() {
    return args -> {
      System.out.println("Runner Bean #2");  // 2
    };
  }

  //@Bean
  @Order(1)
  public ApplicationRunner getRunnerBean3() {
      return args -> {
        System.out.println("Runner Bean #3");  // 3
    };
  }
}
