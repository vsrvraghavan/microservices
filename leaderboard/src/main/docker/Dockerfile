FROM hirokimatsumoto/alpine-openjdk-11
VOLUME /tmp
ADD leaderboard-0.0.1-SNAPSHOT.jar app.jar
ADD onehuddle.properties /etc/onehuddle.properties
EXPOSE 8181
ENV JAVA_OPTS=""
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8181", "-jar","/app.jar", "172.31.42.216","6379"]
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8181", "-jar","/app.jar", "192.168.0.101","6379"]
LABEL maintainer "VSRV Raghavan"  