package ps.exams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
//@EnableWebMvc
@ComponentScan("ps.exams")
public class WebConfig {
    /**
     * 특별한 설정은 없다.
     * 핸들러가 view name을 Return 시
     * Prefix / Suffix를 위한
     * viewResolver만 Bean으로 추가
     *
     * 즉 아래 코드가 없어도 무방한다.
     */

    @Bean
    public UrlBasedViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver ();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
