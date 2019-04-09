#FROM frolvlad/alpine-oraclejdk8:slim
#FROM openjdk:8-jre-alpine
FROM hirokimatsumoto/alpine-openjdk-11
VOLUME /tmp
ADD websocket-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9000
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=9000", "-jar","/app.jar"]
LABEL maintainer "VSRV Raghavan"  