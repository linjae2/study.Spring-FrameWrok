
# [sample-spring-security-microservices](https://github.com/piomin/sample-spring-security-microservices)


## [project : saml ](sample-spring-security-microservices/saml)

### [docker compose 문제점](sample-spring-security-microservices/saml/docker-compose.yml)
- keycloak의 도커 이미지가 인증이 필요함.
- 다른 이미지로 변경해도 TLS 설정 오류로 실행되지 않음

### [pom.xml 미해결](sample-spring-security-microservices/saml/callme-saml/pom.xml)
- java version 21 -> 17 변경시 구문 오류

