FROM websphere-liberty

# Copy the prebuilt war file
ADD target/shopritemobileapplication.war /config/dropins/shopritemobileapplication.war

# Copy over the server environment vars
COPY server.env /config/server.env
COPY keystore /config/resources/security/key.jks
COPY keystore.xml /config/configDropins/defaults/keystore.xml
#COPY --chown=1001:0 server.xml /config/
#COPY --chown=1001:0 target/*.war /config/apps/
#RUN configure.sh
