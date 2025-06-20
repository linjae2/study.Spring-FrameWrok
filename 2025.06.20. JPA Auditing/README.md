
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