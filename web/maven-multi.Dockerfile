FROM maven:3.9-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src src
RUN mvn package

FROM tomcat:10
COPY --from=build /app/target/web.war ${CATALINA_HOME}/webapps/ROOT.war
EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]
