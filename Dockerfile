# WebSphere Liberty
FROM ibmcom/websphere-liberty:20.0.0.3-full-java8-ibmjava-ubi
COPY --chown=1001:0 target/sample-app-api.war /opt/ibm/wlp/usr/servers/defaultServer/apps
COPY --chown=1001:0 server.xml /opt/ibm/wlp/usr/servers/defaultServer
COPY --chown=1001:0 src/dataSource.properties /opt/ibm/wlp/usr/servers/defaultServer/apps
ENTRYPOINT ["/opt/ibm/wlp/bin/server", "run"]
CMD ["defaultServer"]
