FROM amazoncorretto:17-alpine3.18

# 시간 및 날짜 설정
ENV TZ Asia/Seoul

ARG JAR_FILE=build/libs/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]