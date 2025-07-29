# Sprint MVC Tomccat 배포

## 구현제

Interface WebApplicationContext를 구현한 다양한 구현체 중 Annotation 기반의
구현제인 AnnotationConfigWebApplicationContext를 사용한다.

[출처](https://goodgid.github.io/Spring-How-DispatcherServlet-creates-WebApplicationContext/)


## WebApplicationInitializer 인터페이스 구현

```java
public class SpringConfigClass implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("== onstart ==");
    }
}
```

## Bean 지정 클래스 등록

프로젝트 작업시 사용할 Bean을 정의 할 클래스 RootAppContext를 만든다.

```java
@Configuration
public class RootAppContext {
    
}
```

onStartup 메소드에 다음과 같이 추가하여 RootAppContext를 등록한다.

```java
    // Bean을 정의하는 클래스를 지정한다.
    AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
    rootAppContext.register(RootAppContext.class);
    
    ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
    servletContext.addListener(listener);
```
위 코드를 XML 세팅에서 보명 아래와 같다.

```XML
<!-- Bean을 정의 할 xml 파일을 지정한다. -->
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>/WEB-INF/config/root-context.xml</param-value>
</context-param>

<!-- 리스너 설정 -->
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

## 파라미터 인코딩 필터 설정

onStartup 메소드에 다음을 추가한다.

```java
 //파라미터 인코딩 필터 설정 
FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding","utf-8");
        filter.addMappingForServletNames(null,false,"dispatcher");
```
위 코드를 XML 세팅에서 보명 아래와 같다.

```XML
<!-- 파라미터 인코딩 필터 설정 -->
<filter>
    <filter-name>encodingFilter</filter-name>
    <filter-class>
        org.springframework.web.filter.CharacterEncodingFilter
    </filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>

<filter-mapping>
<filter-name>encodingFilter</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping>
```

## 파라미터 인코딩 필터 설정

## Servlet-content.xml --> ServletAppContext.java 

Spring Mvc 프로젝트에 관련된 설정을 하는 클래스인 ServletAppContext를 WebMvcConfigurer인터페이스를 구현해 config 패키지에 생성한다.

```java
// Spring Mvc 프로젝트에 관련된 설정을 하는 클래스
@Configuration

// Controller 어노테이션이 세팅된 클래스를 Controller로 등록
@EnableWebMvc

@ComponentScan("package name")
public class ServletAppcontext implements WebMvcConfigurer{
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // TODO Auto-generated method stub
        WebMvcConfigurer.super.configureViewResolvers(registry);
        registry.jsp("/WEB-INF/views/",".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/**").addResourceLocations("/resources/");
    }
}
```

```java
public class SpringConfigClass implements WebApplicationInitializer{

 @Override
 public void onStartup(ServletContext servletContext) throws ServletException {

    //// Spring Mvc 프로젝트에 관련된 설정을 위해 작성하는 클래스의 객체를 생성한다.
    AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
    servletAppContext.register(ServletAppContext.class);
    }

	
}
```