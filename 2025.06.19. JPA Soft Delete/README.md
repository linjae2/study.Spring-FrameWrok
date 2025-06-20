
# Spring Framework6.2.8

## gradle project 등록

```sh
$ gradle help --task init

$ gradle init --type java-application --no-incubating --no-split-project --java-version 17 --dsl groovy --test-framework junit-jupiter --project-name HelloWorld

# Runs this project as a JVM appliation
$ gradle run
```

## Spring Boot Application 으로 변경

```sh
$ gradle build

# Runs this project as a Spring Boot application
$ gradle bootRun
```

# Test

```http
POST http://localhost:8080/api/greeting
```

```http
PATCH http://localhost:8080/api/greeting
```
```http
DELETE http://localhost:8080/api/greeting
```


```http
GET http://localhost:8080/api/greeting
```