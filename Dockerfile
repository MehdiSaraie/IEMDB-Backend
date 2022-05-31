FROM maven:3.6.3-jdk-11-slim AS maven_builder

COPY pom.xml /build/
COPY src /build/src/

RUN mvn -f /build/pom.xml clean package


FROM tomcat:8.5.76
COPY --from=maven_builder /build/target/ie_phase3-1.0-SNAPSHOT.war /app/iemdb.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

#FROM openjdk:11-jre-slim
#COPY --from=maven_builder /build/target/iemdb-0.0.1-SNAPSHOT.jar /app/iemdb.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/iemdb.jar"]
