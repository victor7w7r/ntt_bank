FROM docker.io/gradle:jdk24-alpine as compile
COPY . /home/source/java
WORKDIR /home/source/java
USER root
RUN chown -R gradle /home/source/java
USER gradle
RUN gradle clean build -x test

FROM docker.io/eclipse-temurin:24-jre-alpine
WORKDIR /home/application/java
COPY --from=compile "/home/source/java/build/libs/customers-1.0.0.jar" .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/home/application/java/customers-1.0.0.jar"]