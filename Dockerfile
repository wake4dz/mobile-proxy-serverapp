# Generate a new WAR file from the repo's code base
FROM maven:3.8.5-eclipse-temurin-11-alpine as build
COPY src /app/src  
COPY pom.xml /app  
RUN mvn -f /app/pom.xml clean install

# Pull base image from https://hub.docker.com/_/websphere-liberty
FROM websphere-liberty:23.0.0.2-kernel-java11-openj9

# Add app and config
COPY --chown=1001:0 --from=build /app/target/wakefernmobileproxy.war /config/dropins/
COPY --chown=1001:0 server.xml /config/

# Default setting for the verbose option
ARG VERBOSE=false

# This script will add the requested XML snippets, grow image to be fit-for-purpose
# https://github.com/WASdev/ci.docker#building-an-application-image
RUN configure.sh