FROM hirokimatsumoto/alpine-openjdk-11
#FROM openjdk:11-jdk-slim  as deploy
#FROM maven:3.5.4-jdk-11-slim
#FROM openjdk:8-jre-alpine
VOLUME /tmp
ADD leaderboard-0.0.1-SNAPSHOT.jar app.jar
ADD onehuddle.properties /etc/onehuddle.properties
EXPOSE 8181
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8181", "-jar","/app.jar", "172.21.0.2","6379"]
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8181", "-jar","/app.jar", "192.168.0.104","6379"]
LABEL maintainer "VSRV Raghavan"  
