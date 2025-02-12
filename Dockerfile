# build stage
FROM maven:3.9.9-eclipse-temurin-23 AS builder
ARG COMPILE_DIR=/compiledir
WORKDIR ${COMPILE_DIR}

COPY mvnw . 
COPY mvnw.cmd . 
COPY pom.xml . 
COPY .mvn .mvn 
COPY src src

RUN chmod +x mvnw

RUN mvn package -e -Dmaven.test.skip=true

# prod stage
FROM eclipse-temurin:23-jre
ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}

COPY --from=builder /compiledir/target/order-system-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# set and expose port 
ENV PORT=8080

EXPOSE ${PORT}

# entry pt
ENTRYPOINT ["java", "-jar", "app.jar"]