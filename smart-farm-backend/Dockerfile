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
FROM openjdk:11-jre-slim
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
COPY /src/main/resources/*.py /root
COPY /src/main/resources/*.h5 /root
COPY /src/main/resources/firebase-credentials.json /root
ENV GOOGLE_APPLICATION_CREDENTIALS /root/firebase-credentials.json
RUN apt-get update && \
	apt install python -y && \
	apt-get install -y python3-pip
RUN pip install numpy tensorflow-cpu pillow
ENV PYSPARK_PYTHON /usr/bin/python3
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
