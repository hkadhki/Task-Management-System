FROM maven:3.8.1-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-oracle
WORKDIR /app
COPY --from=builder /app/target/TaskManagerSystem-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]