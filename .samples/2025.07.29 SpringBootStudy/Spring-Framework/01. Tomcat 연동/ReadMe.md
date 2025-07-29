# Spring Web Application

## web.xml

## Spring boot

### @SpringBootApplication 으로 시작

```java
@SpringBootApplication

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestDemoApplication.class, args);
}
```

### SpringBoot Web Application 설정

 * application.properties 파일 이용

```
server.port=8888 #기본포트외의 다른 포트를 사용하고 싶으면 별도의 포트를 지정하자.

spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```
[Spring MVC, Security 동작 원리와 처리 흐름](https://aaronryu.github.io/2021/02/14/a-tutorial-for-spring-mvc-and-security/)
