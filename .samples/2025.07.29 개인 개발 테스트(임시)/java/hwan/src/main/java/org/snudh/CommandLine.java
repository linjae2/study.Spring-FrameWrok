package org.snudh;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snudh.service.HwanService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CommandLinePropertySource;
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
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class CommandLine {

  //private final static Log log = LogFactory.getLog(CommandLine.class);
  private final static Logger log = LogManager.getLogger(CommandLine.class);

  private final HwanService hwanService;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
    log.info("> ========== Runner Bean #1 ========== ");
    System.out.println("Runner Bean #1");  // 2

    return args -> {
      System.out.println(hwanService.getStandbyInteger(
          LocalDateTime.of(2024, 10, 31, 0, 0, 0).toLocalDate()
        , "29", "P6043"));

      Character ch = hwanService.getVisitCheckValue(
          LocalDateTime.of(2024, 10, 31, 0, 0, 0).toLocalDate()
        , "29", "00317203");
        System.out.println(ch);
    };
  }
}
