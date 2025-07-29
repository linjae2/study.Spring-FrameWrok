

# 도커 이미지 관리

```sh
$> docker compose -f .devcontainer/docker-compose.yaml build
$> docker compose -f .devcontainer/docker-compose.yaml up -d
$> docker compose -f .devcontainer/docker-compose.yaml up discovery-services gateway-service -d
$> docker compose -f .devcontainer/docker-compose.yaml up ocs-services -d
$> docker compose -f .devcontainer/docker-compose.yaml up alim-services

$> docker compose -f .dockers/docker-db.yml down
$> docker compose -f .dockers/docker-db.yml up -d

$> docker compose -f .dockers/docker-db.yml down alim
$> docker compose -f .dockers/docker-db.yml up alim
```

# Eureka Server

```sh
$> gradle java:discovery:bootRun
```

# gateway Server

```http
GET http://172.31.37.51:8080/ocs/api/v1/rsvdate/medis?dday=1
```

```http
GET http://172.31.37.51:8080/ocs/api/v1/rsvdate/exams?dday=1
```

```http
GET http://172.31.37.51:8080/api/v1/rsvdate/exams?dday=4
```

```http
GET http://localhost:8080/ocs/api/hello
```

```http
http://172.31.37.51:8080/api/v1/alimtalk/send
```

```http
http://172.31.37.51:8080/service/token/test/1
```

```http
GET http://172.31.37.51:8080/api/v1/alimtalk/token/eyJwdG4iOiIwMTA2MDA4NSIsIm1kdCI6MTcyNzIyMjQwMCwibWRkIjoiMDEifQ.X3v3sdv-XvkhkSvH2sq3WmT2t3O5pEgKALW44weYm50
```

```http
# 예약 정보 가져오기
GET http://172.31.37.51:8080/api/v1/rsvdate/RsvDrSchDay/R1414/2024-10-08/04/30/Y
```

### 도커 이미지 만들기

```sh
$> docker compose -f .devcontainer/docker-compose.yaml build
$> docker compose -f .devcontainer/docker-compose.yaml up discovery-services
```

# OCS Service

```sh
$> gradle java:ocs:bootRun
```

```http
GET http://172.31.37.16:8001/api/v1/rsvdate/medis?dday=2
```

```http
GET http://localhost:8001/api/v1/rsvdate/exams?dday=4
```

```http
GET http://localhost:8001/api/hello
```


```http
GET http://localhost:8001/api/v1/rsvdate/schedule/P6059/40/2024-10
```


```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvInfo/01071276/2024-10-04T08:30:00/03
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSch/01071276/2024-10-04T08:30:00/03
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSch/00730328/2024-08-17T10:30:00/08
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSch/01069829/2024-08-01T11:00:00/01
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSch/00265254/2024-10-11T15:30:00/41
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSch/00817322/2024-10-07T10:00:00/04
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSchDay/R1414/2024-10-16/04/30/N
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSchMonth/R1414/2024-10/04/30/Y
```


```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSchMonth/R1414/2026-04/04/30/Y
```


```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSchDay/P6054/2024-11-12/41/30/Y
```

```http
# 예약 정보 가져오기
GET http://172.31.37.16:8001/api/v1/rsvdate/RsvDrSchDay/R1414/2024-10-08/04/30/Y
```


# Alim Service

```http
GET http://localhost:8021/api/v1/rsvdate/medis?dday=8
```

```http
GET http://localhost:8021/api/v1/rsvdate/medis
```

```http
GET http://localhost:8021/api/v1/rsvdate/register?dday=21
```


```http
GET http://localhost:8021/api/v1/rsvdate/register10?dday=1
```

```http
GET http://172.31.37.16:8021/api/hello
```


```http
POST http://localhost:8021/api/v1/rsvdate/register
Content-Type: application/json

{
  "rsvDay": "2024-08-17",
  "dday": 2,
  "msgType": "진료",
  "msgData": null
}
```

```http
GET http://localhost:8021/api/v1/alimtalk/register/medis?dday=1
```

```http
GET http://172.31.37.51:8021/api/v1/alimtalk/register/medis?dday=1
```

```http
GET http://localhost:8021/api/v1/alimtalk/token/eyJwdG4iOiIwMDk5OTk3OCIsIm1kdCI6MTcyNzIyMjQwMCwibWRkIjoiMDEifQ.abmX0m5_pk6XVV1Awtcf2WL2xTroWOwVqMReaz9WzYA
```

```http
GET http://localhost:8021/api/v1/alimtalk/token/eyJwdG4iOiIwMTA2MDA4NSIsIm1kdCI6MTcyNzIyMjQwMCwibWRkIjoiMDEifQ.X3v3sdv-XvkhkSvH2sq3WmT2t3O5pEgKALW44weYm50
```