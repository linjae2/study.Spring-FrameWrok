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
         * Config를 등록하는데 2가지 방법이 있다.
         * --
         * 1. context.setConfigLocation("ps.exams.config.ServletAppContext")
         * 2. context.register(ServletAppContext.class);
         */
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
