# Generate a new WAR file from the repo's code base
# FROM maven:3.5-jdk-8-alpine as build
FROM maven:3.8.5-eclipse-temurin-11-alpine as build
COPY src /app/src  
COPY pom.xml /app  
RUN mvn -f /app/pom.xml clean install

# Pull base image from https://hub.docker.com/_/websphere-liberty
FROM websphere-liberty:23.0.0.2-full-java11-openj9
COPY --chown=1001:0 server.xml /config/
COPY --chown=1001:0  --from=build /app/target/shopritemobileapplication.war /config/dropins/