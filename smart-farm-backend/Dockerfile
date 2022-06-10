#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build  
COPY src /usr/src/app/src 
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jdk-alpine  
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar  
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/usr/app/app.jar"] 