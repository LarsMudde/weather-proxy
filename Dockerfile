FROM maven:3.6.3-jdk-8-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /usr/src/app/target/weatherproxy-0.0.1-SNAPSHOT.jar /usr/app/weatherproxy-0.0.1-SNAPSHOT.jar
EXPOSE 7878
ENTRYPOINT ["java","-jar","/usr/app/weatherproxy-0.0.1-SNAPSHOT.jar"]
