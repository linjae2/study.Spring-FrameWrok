
- 인증서 생성

```bash
$ keytool -genkey -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650 -storepass test**
$ keytool -genkeypair -alias mysslkey -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650


$ keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype JKS -keystore springboot.jks -validity 3650 -storepass password
$ keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springboot.p12 -validity 3650 -storepass password

$ scp injae@172.31.37.55:/data/src/gitlab/keystore.p12 ./src/main/resources/

$ curl -I -k https://localhost:8443

```

```http
GET http://localhost:8080/
```

```http
GET https://localhost:8443/
```


## 크롬 브라우저가 localhost 에서 안전하지 않은 연결을 신뢰하도록 하기

```
chrome://flags/#allow-insecure-localhost
```


