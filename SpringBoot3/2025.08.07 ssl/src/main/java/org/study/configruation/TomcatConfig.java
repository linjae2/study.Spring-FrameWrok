package org.study.configruation;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
  /*
   * 방법 1(리다이렉트 되지 않았다. ㅠㅠ)
   */
  // @Bean
  // public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
  //   return factory -> {
  //     Connector httpConnector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
  //     httpConnector.setPort(8080); // Or your desired HTTP port
  //     httpConnector.setRedirectPort(8443); // The HTTPS port
  //     factory.addAdditionalTomcatConnectors(httpConnector);
  //   };
  // }

  /*
   * 방법 2
   */
  @Bean
  public ServletWebServerFactory servletContainer() {
      TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
          @Override
          protected void postProcessContext(org.apache.catalina.Context context) {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();

            // 특정 경로에 대해서만 https로 리다이렉트시키기
            // collection.addPattern("/test");

            collection.addPattern("/*");

            securityConstraint.addCollection(collection);
            context.addConstraint(securityConstraint);
          }
      };
      tomcat.addAdditionalTomcatConnectors(createHttpConnector());
      return tomcat;
  }

  private Connector createHttpConnector() {
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    connector.setScheme("http");
    connector.setPort(8080); // HTTP port
    connector.setSecure(false);
    connector.setRedirectPort(8443); // HTTPS port
    return connector;
  }
}
