# syntax=docker/dockerfile:experimental
FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
RUN mkdir -p target/dependency/lib && (cd target/dependency; cp $(java -jar ../*.jar --thin.classpath | tr : '\n') lib)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/lib /app/lib
COPY --from=build ${DEPENDENCY}/../*.jar /app/lib/app.jar
ENTRYPOINT ["java","-cp","app/lib/*","org.springframework.samples.petclinic.PetClinicApplication"]
