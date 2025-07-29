# AbstractAnnotationConfigDispatcherServletInitializer 상속하여 web.xml 작성

WebApplicationInitializer 인터페이스 구현 외에
AbstractAnnotationConfigDispatcherServletInitializer 상속하는 방법도 있다.

* DispatcherServlet에 매핑할 요청 주소를 세팅

```java
//기존 WebApplicationInitializer - 
servlet.setLoadOnStartup(1);
servlet.addMapping("/");
```

```java
// 변경
@Override
protected String[] getServletMappings() {
    return new String[] {"/"};
}
```

* Spring MVC 프로젝트 설정을 위한 클래스 지정

```java
/// 기존 WebApplicationInitializer -
AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
servletAppContext.register(ServletAppcontext.class);

DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
```

```java
// 변경
@Override
protected Class<?>[] getServletConfigClasses() {
    return new Class[] {ServletAppcontext.class};
}
```
* 프로젝트에서 사용할 bean들을 정의하기 위한 클래스 지정

```java
// 기존 WebApplicationInitializer -

AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
rootAppContext.register(RootAppContext.class);

ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
servletContext.addListener(listener);
```

```java
// 변경
@Override
protected Class<?>[] getRootConfigClasses() {
    return new Class[] {RootAppContext.class};
}
```
* 
* 프로젝트에서 사용할 bean들을 정의하기 위한 클래스 지정

```java
// 기존 WebApplicationInitializer -

FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
filter.setInitParameter("encoding","utf-8");
filter.addMappingForServletNames(null,false,"dispatcher");
```

```java
// 변경
@Override
protected Filter[] getServletFilters() {
    CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
    encodingFilter.setEncoding("UTF-8");
    return new Filter[] {encodingFilter};
}
```


## 전체 코드

```java
public class SpringConfigClass extends AbstractAnnotationConfigDispatcherServletInitializer{
	//DispatcherServlet에 매핑할 요청 주소를 세팅한다.
	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[] {"/"};
	}
	//Spring MVC 프로젝트 설정을 위한 클래스 지정
	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {ServletAppcontext.class};
	}
	//프로젝트에서 사용할 bean들을 정의하기 위한 클래스 지정
	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {RootAppContext.class};
	}
	
	//파라미터 인코딩 필터 설정
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		return new Filter[] {encodingFilter};
	}
}
```


그외 참고 : [순수 java 코드를 이용한 Spring-MVC 등록 과정](https://escapefromcoding.tistory.com/174)