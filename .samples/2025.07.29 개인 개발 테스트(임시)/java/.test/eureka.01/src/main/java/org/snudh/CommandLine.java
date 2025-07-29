package org.snudh;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

import org.snudh.domain.RsvAlim;
import org.snudh.domain.RsvInfoDto;
import org.snudh.domain.SchedAlim;
import org.snudh.service.AlimService;
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

  private final AlimService service;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public class User {
    private String name;
    private int age;
    private String city;
  }

  //@Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
      return args -> {
        String jsonstr = """
{
  "auth_code" : "1P7cUq9bYyIanKlgTRj3yQ=="
, "sender_key":"26dc6c15158be6a70a4363dea7733e43cb833f25"
, "nation_phone_number":"82"
, "phone_number":"01072006269"
, "template_code":"mts032"
, "subject": "진료예약 알림"
, "message":"[서울대학교치과병원]\\n\\n홍지연 (등록번호- 00252350)님 08/12(월) 오후 2시  구강내과(2층) 장지희교수 진료예약\\n\\n[예약 취소 및 변경]\\n\\n전화 : 1522-2700\\n\\n■ 본인-신분증 확인 의무화(24년 5월 20일부터 신분증 지참 필수)\\n\\n   불편하시더라도 꼭 신분증을 지참하여 주시길 부탁드립니다\\n   (※ 신분증 미지참시 진료비 전액을 본인부담하셔야 합니다)\\n\\n● 진료비 자동결제(바로결제) 서비스 운영 안내\\n\\n신용카드를 미리 수납창구에서 신청등록하시면 이후에는 수납창구 방문없이 진료비가 다음날 자동결제 됩니다\\n\\n감사합니다."
, "callback_number":"1522-2700"
}""";

        // System.out.println("Runner Bean #1");  // 1
        // String result = restClient.post()
        //   .uri("/atk/sendMessage")
        //   .contentType(MediaType.APPLICATION_JSON)
        //   .body(jsonstr)
        //   .retrieve().body(String.class);

        //   System.out.println("Runner Bean #1 : " + jsonstr);
        //   System.out.println("Runner Bean #1 : " + result);
      };
  }

  public record TestBDto(LocalDate rsvDay, Integer dday, String msgType, String msgData) {}

  //@Bean
  public CommandLineRunner getRunnerBean2() {
    return args -> {
      System.out.println("Runner Bean #2");  // 2

      RestClient restClient = RestClient.builder()
      //.baseUrl("")
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

      if (service.existsPopedData()) {
        System.out.println("============================");
      }

      //SchedAlim alim = service.topData();
      Integer dday = 3;
      String p1 = restClient.get()
        .uri(String.format("http://172.31.37.51:8001/api/v1/rsvdate/medis?dday=%d", dday))
        .retrieve()
        .body(String.class);

      List<RsvInfoDto> ri = restClient.get()
        .uri(String.format("http://172.31.37.51:8001/api/v1/rsvdate/medis?dday=%d", dday))
        .retrieve()
        .body(new ParameterizedTypeReference<List<RsvInfoDto>>() {});

      TestBDto dtaDto1 = new TestBDto(LocalDate.now().plusDays(dday), dday, "진료", p1);
      restClient.post()
        .uri("http://localhost:8021/api/v1/rsvdate/register")
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtaDto1).retrieve().toBodilessEntity();

      if (service.existsPopedData()) {
        System.out.println("============================");
        SchedAlim alim = service.topData();
        SchedAlim.Key key = alim.getKey();
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        List<RsvInfoDto> footballTeamMap = objectMapper.readValue(alim.getMsgData(), new TypeReference<>(){});
        footballTeamMap.stream().forEach(item -> {
          //System.out.println("==== > " + (String)item.get("patno"));
          System.out.println("==== > " + item.getPatname());
          service.putRsvAlim(key, item);
          // try {
          //   //Thread.sleep(2000);
          // } catch (InterruptedException e) {
          // }

          service.putMediRsvTalk(item);
        });
      }
      System.out.println("======================= ");
    };
  }

  //@Bean
  @Order(1)
  public ApplicationRunner getRunnerBean3() {
      return args -> {
        System.out.println("Runner Bean #3");  // 3

String tx1 = """
[서울대학교치과병원]
<`eddate, dayhosp)> <deptname> <meddr> <fmedtext(meddate, dayhosp)>

[예약 취소 및 변경]

전화 : #{진료과전화번호}

■ 본인-신분증 확인 의무화(24년 5월 20일부터 신분증 지참 필수)

   불편하시더라도 꼭 신분증을 지참하여 주시길 부탁드립니다
   (※ 신분증 미지참시 진료비 전액을 본인부담하셔야 합니다)

● 진료비 자동결제(바로결제) 서비스 운영안내

  신용카드를 미리 수납창구에서 신청등록하시면 이후에는 수납창구 방문없이 진료비가 다음날 자동결제 됩니다

감사합니다.
""";

String tx = """
[서울대학교치과병원]
<patname>(등록번호- <patno>)님 <fmeddate(meddate, dayhosp)> <deptname> <meddr> 테스트

<test(meddate)>
TEST TEST""";

          String template = """
fmeddate(meddate, dayhosp) ::= <<
<meddate; format="MM/dd(E)"> 이건뭐.. <dayhosp> 출력 <if(dayhosp)> <meddate; format="a h시"><endif>
>>

test(meddate) ::= <<
<if(fmintext(meddate))>
I'am
<else>
You'ar
<endif>
>>

fmintext(meddate) ::= <<
<meddate; format="mm">
>>

fmedtext(meddate, dayhosp) ::= <<
<meddate; format="MM/dd(E)">
>>
""";
    };
  }
}
