FROM maven:3.9-eclipse-temurin-8 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:8-jdk-jammy AS run

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]