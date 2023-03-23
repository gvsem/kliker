FROM maven:3.8.1-openjdk-17-slim

COPY pom.xml /tmp/
RUN mvn -B dependency:go-offline -f /tmp/pom.xml

COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn -B clean package
