package org.snudh.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.snudh.domain.SchedDto;
import org.snudh.service.AlimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rsvdate")
@RequiredArgsConstructor
public class RsvDateController {

  private final AlimService service;

  public record TestBDto(LocalDate rsvDay, Integer dDay, String msgType, String msgData) {}

  @PostMapping("/register")
  public ResponseEntity<Void> postRegister(
    @RequestBody SchedDto data
  ) {
    service.putSchedAlim(data);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/medis")
  public ResponseEntity<TestBDto> getMedis(
    @RequestParam(name ="dday", defaultValue = "1") Integer dday
  ) {
    System.out.println("Runner Bean #2");  // 2

    RestClient restClient = RestClient.builder()
    .messageConverters(httpMessageConverters -> {
      httpMessageConverters.stream()
      .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
      .map(
        converter -> (MappingJackson2HttpMessageConverter) converter).forEach(converter -> {
          ObjectMapper objectMapper = converter.getObjectMapper();
          objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
          //converter.setObjectMapper(customObjectMapper());

          DeserializerFactory factory = objectMapper.getDeserializationContext().getFactory();

          //factory.SimpleDeserializers
          SimpleDeserializers desers = new SimpleDeserializers();

          converter.setPrettyPrint(true);
          List<MediaType> mediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
          mediaTypes.add(MediaType.APPLICATION_JSON);
          converter.setSupportedMediaTypes(mediaTypes);
        });
    })
    .build();

    String pp = restClient.get()
      .uri(String.format("http://172.31.37.51:8001/api/v1/rsvdate/medis?dday=%d", dday))
      .retrieve()
      .body(String.class);

    TestBDto dtaDto = new TestBDto(LocalDate.now().plusDays(dday), dday, "진료", null);
    return ResponseEntity.ok().body(dtaDto);
  }

  @GetMapping("/send")
  public ResponseEntity<Void> getSned() {
    return ResponseEntity.ok().build();
  }
}
