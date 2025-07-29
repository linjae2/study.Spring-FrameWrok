package ps.exams;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ps.exams.config.RootConfig;
import ps.exams.config.ServletConfig;
import ps.exams.security.SecurityConfig;

public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(jakarta.servlet.ServletContext servletContext) throws ServletException {

        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class, SecurityConfig.class);
        //rootContext.register(RootConfig.class);
        rootContext.refresh();

        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Create and register the DispatcherServlet
        AnnotationConfigWebApplicationContext servletRegisterer = new AnnotationConfigWebApplicationContext();
        servletRegisterer.register(ServletConfig.class);
        ServletRegistration.Dynamic registration = servletContext.addServlet("servlet",
                new DispatcherServlet(servletRegisterer));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
