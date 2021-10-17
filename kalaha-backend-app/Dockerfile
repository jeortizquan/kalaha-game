FROM maven:3.8.3-openjdk-11 as base
MAINTAINER  Jorge Ortiz <jeortiz.quan@gmail.com>
WORKDIR /app

COPY pom.xml ./
COPY src ./src

FROM base as test
RUN ["mvn", "test"]

FROM base as development
CMD ["mvn", "spring-boot:run"]

FROM base as build
RUN mvn package

FROM openjdk:11-jre-slim as production
EXPOSE 8080

COPY --from=build /app/target/kalah-0.0.1-*.jar /kalah.jar

CMD ["java", "-jar", "/kalah.jar"]