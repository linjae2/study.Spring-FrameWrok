
- 인증서 생성

```bash
$ keytool -genkeypair -alias mysslkey -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

$ scp injae@172.31.37.55:/data/src/gitlab/keystore.p12 ./src/main/resources/

$ curl -I -k https://localhost:8443

```

```http
GET http://localhost:8443/
```

```http
GET https://localhost:8443/
```