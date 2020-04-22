FROM java:8
MAINTAINER lqc520/lnchengzi@foxmail.com
VOLUME /tmp
ADD target/gateway-1.0.0-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8888
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]


