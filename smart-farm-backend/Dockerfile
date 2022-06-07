#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY . /home/app/
RUN mvn -f /home/app/pom.xml clean install

#
# Package stage
#
FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]