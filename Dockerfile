FROM amazoncorretto:17-alpine-jdk
COPY build/Buen-Sabor-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]