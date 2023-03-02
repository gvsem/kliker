FROM maven:3.8.1-openjdk-17-slim AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
RUN mvn -B dependency:go-offline -f /tmp/pom.xml

COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn -B clean package

FROM openjdk:17-jdk-slim AS JRE

FROM scratch

COPY --from=JRE /lib /lib
COPY --from=JRE /lib64 /lib64
COPY --from=JRE /tmp /tmp
COPY --from=JRE /usr/local/openjdk-17 /usr/local/openjdk-17
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar /spring-boot-application.jar

EXPOSE 7890
CMD ["/usr/local/openjdk-17/bin/java","-jar","/spring-boot-application.jar"]
