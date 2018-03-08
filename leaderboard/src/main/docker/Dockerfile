FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD leaderboard-0.0.1-SNAPSHOT.jar app.jar
ADD onehuddle.properties /etc/onehuddle.properties
EXPOSE 8181
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8181", "-jar","/app.jar", "172.18.0.1","6379"]
LABEL maintainer "VSRV Raghavan"  