package org.snudh;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.snudh.dto.MediBook;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestClient;
import org.stringtemplate.v4.DateRenderer;
import org.stringtemplate.v4.NumberRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

@Configuration
public class CommandLine {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CommandLineRunner getRunnerBean1() {
      return args -> {
        System.out.println("Runner Bean #1");  // 1
      };
    }

    @Bean
    public CommandLineRunner getRunnerBean2() {
      return args -> {
        System.out.println("Runner Bean #2");  // 2
      };
    }

  @Bean
  @Order(1)
  public ApplicationRunner getRunnerBean3() {
    return args -> {
String tx = """
[서울대학교치과병원]
<medi.patname> (등록번호- <medi.patno>)님 <medi.toMeddate> <medi.deptname> <medi.toMeddr><medi.etc>

[예약 취소 및 변경]

전화 : <medi.sendno>

■ 본인-신분증 확인 의무화(24년 5월 20일부터 신분증 지참 필수)

   불편하시더라도 꼭 신분증을 지참하여 주시길 부탁드립니다
   (※ 신분증 미지참시 진료비 전액을 본인부담하셔야 합니다)

● 진료비 자동결제(바로결제) 서비스 운영안내

  신용카드를 미리 수납창구에서 신청등록하시면 이후에는 수납창구 방문없이 진료비가 다음날 자동결제 됩니다

감사합니다.
""";

    RestClient restClient = RestClient.builder()
      .baseUrl("http://172.31.37.51:8080/test")
      // .messageConverters(httpMessageConverters -> {
      //   httpMessageConverters.stream()
      //   .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
      //   .map(
      //     converter -> (MappingJackson2HttpMessageConverter) converter).forEach(converter -> {
      //       ObjectMapper objectMapper = converter.getObjectMapper();
      //       objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
      //       //converter.setObjectMapper(customObjectMapper());
      //       DeserializerFactory factory = objectMapper.getDeserializationContext().getFactory();
      //       //factory.SimpleDeserializers
      //       SimpleDeserializers desers = new SimpleDeserializers();
      //       converter.setPrettyPrint(true);
      //       List<MediaType> mediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
      //       mediaTypes.add(MediaType.APPLICATION_JSON);
      //       converter.setSupportedMediaTypes(mediaTypes);
      //     });
      // })
      .build();

      List<MediBook> pp = restClient.get()
        .uri("/api/ocs/dday/{dday}", 1)
        .retrieve()
        .body(new ParameterizedTypeReference<List<MediBook>>() {
        });

      pp.stream().forEach(medi-> {
        ST st = new ST(tx);
        st.add("medi", medi);
        String result = st.render(new Locale("ko"));
        System.out.println("Runner Bean #1:" + result);  // 1
      });
    };
  }
}
