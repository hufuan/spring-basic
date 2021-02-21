FROM openjdk:11.0.3-stretch
MAINTAINER fuhu <fuan_707@163.com>
VOLUME /tmp
COPY target/h2-mybatis-docker-0.0.1-SNAPSHOT.jar demo-1.0.0.jar
RUN bash -c "touch /demo-1.0.0.jar"
EXPOSE 8086
ENTRYPOINT ["java","-jar","demo-1.0.0.jar"]