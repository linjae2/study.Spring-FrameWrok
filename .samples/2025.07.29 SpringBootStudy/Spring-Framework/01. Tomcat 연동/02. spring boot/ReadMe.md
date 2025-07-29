# Sprint MVC Tomccat 배포 ( Maven 프로젝트)

프로젝트와 Tomcat 연동을 위하여 별도로 한게 없다. ㅠㅠ


- [ ] spring boot 에서 jsp 사용의 위한 

스프링 브트에서는 jsp 사용하는 것을 건장하지 않고 있어, 내장된 톱켓은 JSP 엔진을 포함하지 않는다.

의존성 추가 => tomcat-embed-jasper

```
implementation "org.apache.tomcat.embed:tomcat-embed-jasper"
```