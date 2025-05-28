FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/wallet-service-assignment-*.jar app.jar
RUN mkdir logs

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
