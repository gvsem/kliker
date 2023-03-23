FROM kliker:system AS system
FROM kliker:build AS build

FROM scratch

COPY --from=system /lib /lib
COPY --from=system /lib64 /lib64
COPY --from=system /tmp /tmp
COPY --from=system /usr/local/openjdk-17 /usr/local/openjdk-17
COPY --from=build /tmp/target/*.jar /spring-boot-application.jar

EXPOSE 7890
CMD ["/usr/local/openjdk-17/bin/java","-jar","/spring-boot-application.jar"]
