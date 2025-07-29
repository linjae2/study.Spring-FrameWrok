package org.snudh;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.SchedDto;
import org.snudh.service.TestService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
public class CommandLine {

  private static final Logger log = LoggerFactory.getLogger(CommandLine.class);

  //private final RestClient restClient;
  private final DiscoveryClient discoveryClient;
  private final TestService service;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
    return args -> {
      //System.out.println("Runner Bean #1");  // 1
      log.debug("=================== Runner Bean #1 ==============");

      // //SecretKey key = Kyes.secretKeyFor(SignatureAlgorithm.HS256);
      // // SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode("secretKey"));
      SecretKey key = Jwts.SIG.HS256.key().build();
      // String secretString = Encoders.BASE64.encode(key.getEncoded());

      // LocalDateTime now = LocalDateTime.now();
      // long issueEpoch = now.atZone(ZoneId.systemDefault()).toEpochSecond();

      // String tocken = Jwts.builder()
      // .claim("pn", "12345678")
      // .claim("iat", issueEpoch)
      // .signWith(key)
      // .compact();

      // System.out.println("key : " + secretString);
      // System.out.println(tocken);
    };
  }

  @Bean
  public CommandLineRunner getRunnerBean2() {
    return args -> {
      System.out.println("Runner Bean #2");  // 2
      RestClient restClient = RestClient.builder().build();

      ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);

      Integer dday = 4;
      String rs1 = restClient.get()
      .uri(String.format("%s/api/v1/rsvdate/medis?dday=%d", serviceInstance.getUri(), dday))
      .retrieve()
      .body(String.class);

      SchedDto dt = SchedDto.builder()
              .rsvDate(LocalDate.now().plusDays(dday))
              .dday(dday).msgType("진료").msgData(rs1).build();
      //return new ResponseEntity<>(serviceAResponse, HttpStatus.OK);
      //service.putSchedAlim(dt);
      service.putSchedAlim(dt);
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
