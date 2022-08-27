#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build  
COPY src /usr/src/app/src 
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
COPY /src/main/resources/*.py ./
COPY /src/main/resources/*.h5 ./
COPY /src/main/resources/firebase-credentials.json /root
COPY /src/main/resources/command.sh ./

ENV GOOGLE_APPLICATION_CREDENTIALS /root/firebase-credentials.json

RUN apt-get update && \
	apt install python -y && \
	apt-get install -y python3-pip
RUN pip install numpy tensorflow-cpu pillow
RUN pip install fastapi
RUN pip install "uvicorn[standard]"

RUN chmod +x ./command.sh

CMD ./command.sh

EXPOSE 8080
#CMD ["uvicorn", "healthPredict:app"]
#ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
