

Logger.console.pattenr :

"%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

"%clr{%d{yyyy-MM-dd'T'HH:mm:ss.SSSXXX}}{faint} %clr{%5p} %clr{%pid}{magenta} %clr{--- %esb{test-program}%esb{}[%15.15t] }{faint}%clr{%-40.40c{1.}}{cyan} %clr{:}{faint} %m%n%xwEx"



"%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
"%clr{%d{yyyy-MM-dd'T'HH:mm:ss.SSSXXX}}{faint} %clr{%5p} %clr{%pid}{magenta} %clr{--- %esb{test-program}%esb{}[%15.15t] }{faint}%clr{%-40.40c{1.}}{cyan} %clr{:}{faint} %m%n%xwEx"

"%clr{%d{yyyy-MM-dd'T'HH:mm:ss.SSSXXX}}{faint} %clr{%5p} %clr{%pid}{magenta} %clr{--- %esb{test-program}%esb{}[%15.15t] }{faint}%clr{%-40.40c{1.}}{cyan} %clr{:}{faint} %m%n%xwEx"

final Layout<? extends Serializable> layout = PatternLayout.newBuilder()
        .withPattern(DefaultConfiguration.DEFAULT_PATTERN)
        .withConfiguration(this)
        .build();

ElasticCommonSchemaStructuredLogFormatter

    public static final String DEFAULT_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";


./mvnw clean install spring-boot:run


"jar:file:/C:/Users/in-jae/.gradle/caches/modules-2/files-2.1/org.springframework.boot/spring-boot/3.4.4/f9dbe14c2e5e35a2cd27156802ea6b7c42ab34fd
/spring-boot-3.4.4.jar!/org/springframework/boot/logging/log4j2/log4j2.xml"