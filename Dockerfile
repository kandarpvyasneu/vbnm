FROM maven:3.5-jdk-8 AS build

RUN mkdir /cloud_application

COPY src /cloud_application/src
COPY pom.xml /cloud_application/pom.xml

RUN mvn -f /cloud_application/pom.xml clean install -DskipTests=true

FROM tomcat

COPY --from=build /cloud_application/target/demo.war /usr/local/tomcat/webapps/

EXPOSE 8080

