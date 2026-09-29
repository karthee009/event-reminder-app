FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# application.properties is gitignored, so use the secret-free .example during the cloud build
RUN cp src/main/resources/application.properties.example src/main/resources/application.properties
RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV TZ=Asia/Kolkata
EXPOSE 8097
ENTRYPOINT ["java","-jar","app.jar"]
