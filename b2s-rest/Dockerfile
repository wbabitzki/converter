FROM payara/micro

EXPOSE 8080 5005

COPY ./target/b2s-rest-1.0-SNAPSHOT.war /opt/payara/deployments/b2s-rest.war

ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar /opt/payara/payara-micro.jar --deploymentDir /opt/payara/deployments