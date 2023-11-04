FROM gradle:8.0-jdk17 as build
WORKDIR /app
RUN chown -R gradle:gradle /app
USER gradle
COPY build.gradle .
COPY src src
RUN gradle build

FROM tomcat:10
COPY --from=build /app/build/libs/web.war ${CATALINA_HOME}/webapps/ROOT.war
EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]
