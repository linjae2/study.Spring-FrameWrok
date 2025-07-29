# Sprint MVC Tomccat 배포


WebConfig 에서
@EnableWebMvc 어노테이션을 사용하려면

```java
# src/main/java/ps/emams/config/WebConfig.java

@Configuration
//@EnableWebMvc
@ComponentScan("ps.exams")
public class WebConfig {

```
위와 같이 @EnableWebMvc 를 주석처리해도 잘 실행된다.

무슨의미????

reference :
[git clone -b 13-@EnableWebMvc https://github.com/goodGid/Inflearn-Web-MVC.git](https://github.com/goodGid/Inflearn-Web-MVC/tree/13-%40EnableWebMvc)
