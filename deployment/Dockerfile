#Construccion del JAR
FROM gradle:8.14.3-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test -x validateStructure --no-daemon

#Construccion de la imagen
#FROM amazoncorretto:21-alpine-jdk
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

###############################################################################

#FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
#COPY *.jar CrediYa_Ms_Autenticacion.jar
#ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Replace with a non-root user to avoid running the container with excessive privileges
#USER appuser
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar CrediYa_Ms_Autenticacion.jar" ]
