# Spring Security 등록

 * AbstractSecurityWebApplicationInitializer



 * Spring Container 에서 springSecurityFilterChain 이라는 이름을 가진 Bean을 찾는다. 이때 클라이언트의 요청을 위임받는 객체는 FilterChainProxy 이다.
 * DelegatingFilterProxy는 클라이언트의 요청을 위임하고, 해당 요청은 FilterChainProxy의 doFilter() 메서드로 넘어간다.


## Spring Security의 Filters

### 특징

Spring Security의 Filter들은 각기 다른 역할을 수행한다.
필요한 Filter만 적용할 수 있으며, Filter들의 순서를 조정할 수 있다.

### 종류

- SecurityContextPersistenceFilter

SecurityContextRepository에서 SecurityContext를 가져와 유저의 Authentication에 접근할 수 있도록 하는 Filter

- LogoutFilter

로그아웃 요청을 처리하는 Filter

- UsernamePasswordAuthenticationFilter

ID와 PW를 사용하는 Form기반 유저 인증을 처리하는 Filter

- DefaultLoginPageGeneratingFilter

커스텀 로그인 페이지를 지정하지 않았을 경우, 기본 로그인 페이지를 반환하는 Filter

- AnonymousAuthenticationFilter

이 Filter가 호출되는 시점까지 사용자가 인증되지 않았다면, 익명 사용자 토큰을 반환하는 Filter

- ExceptionTranslationFilter

Filter Chain 내에서 발생된 모든 예외를 처리하는 Filter

- FilterSecurityInterceptor

권한 부여와 관련된 결정을 AccessDecisionManager에게 위임해 권한 부여 및 접근 제어를 처리하는 Filter

- RequestCacheAwareFilter

로그인 성공 후, 이전 요청 정보를 재구성 하기위해 사용하는 Filter

- SessionManagementFilter

로그인 이후 인증된 사용자인지 확인하거나, 설정된 세션의 메커니즘에 따라 작업을 수행하는 Filter

- BasicAuthenticationFilter

HTTP 요청의 인증 헤더를 처리하여 결과를 SecurityContextHolder에 저장하는 Filter

- RememberMeAuthenticationFilter

세션이 사라지거나 만료 되더라도 쿠키 또는 DB를 사용하여 저장된 토큰 기반으로 인증을 처리하는 Filter





