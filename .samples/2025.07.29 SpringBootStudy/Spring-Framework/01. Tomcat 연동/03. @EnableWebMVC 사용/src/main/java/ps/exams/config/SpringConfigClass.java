package ps.exams.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringConfigClass implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("===== onStartup()");

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        /**
         *
         * WebConfig에서
         * @EnableWebMvc 어노테이션을 사용하려면
         * context.setServletContext() 코드가 반드시 필요하다.
         * Reference : be.goodgid.WebConfig
         *
         * ## Why?
         * @EnableWebMvc -> DelegatingWebMvcConfiguration -> WebMvcConfigurationSupport
         * 경로를 따라 들어가면
         * servletContext를 참조하는 곳이 있다.
         * 그렇기 때문에
         * ServletContext을 설정해주지 않으면
         * 정상적으로
         * Bean 설정이 되지 않는다.
         * Reference : org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
         */
        context.setServletContext(servletContext);
        context.register(WebConfig.class);
        context.refresh();

        /**
         위에서 생성한 AnnotationConfigWebApplicationContext 인스턴스를
         DispatcherServlet 생성자 값으로 전달한다.

         public DispatcherServlet(WebApplicationContext webApplicationContext) {
         super(webApplicationContext);
         this.setDispatchOptionsRequest(true);
         }

         Reference : org.springframework.web.servlet.DispatcherServlet#
                          DispatcherServlet(org.springframework.web.context.WebApplicationContext)
         */
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);

        //부가 설정
        servlet.setLoadOnStartup(1);                // <load-on-startup>1</load-on-startup>
        servlet.addMapping("/");
    }
}
