# [Memo] Pluralsights lesson - Developing Java Apps with Docker

> Developing Java Apps with Docker
> by Esteban Herrera
> https://app.pluralsight.com/library/courses/java-apps-docker-developing-2023/table-of-contents

<!-- TOC -->
* [[Memo] Pluralsights lesson - Developing Java Apps with Docker](#memo-pluralsights-lesson---developing-java-apps-with-docker)
  * [Using Docker to build and run java program](#using-docker-to-build-and-run-java-program)
  * [Using Dockerfile to build image and run .jar program](#using-dockerfile-to-build-image-and-run-jar-program)
  * [Using Dockerfile to build image and run .war program](#using-dockerfile-to-build-image-and-run-war-program)
  * [Using Dockerfile to build maven project and image, then run it](#using-dockerfile-to-build-maven-project-and-image-then-run-it)
  * [Using Dockerfile to build gradle project and image, then run it](#using-dockerfile-to-build-gradle-project-and-image-then-run-it)
  * [Ref.](#ref)
<!-- TOC -->

## Using Docker to build and run java program

```bash
$ mkdir hello
$ cd hello
$ vi ./Hello.java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}

$ docker run --rm -v ${PWD}:/hello -w /hello amazoncorretto:8 javac Hello.java # generate Hello.class to current work directory 
$ docker run --rm -v ${PWD}:/hello -w /hello amazoncorretto:8 java Hello
Hello World
$ docker run --rm -v ${PWD}:/hello -w /hello amazoncorretto:19 java Hello
Hello World
```
## Using Dockerfile to build image and run .jar program

```bash
# docker cp <container>:/app/target/api.jar .
$ ls api.jar
api.jar
$ vi jar.Dockerfile
FROM eclipse-temurin:17
RUN mkdir /app
#RUN ["executable", "param1", "param2"]
WORKDIR /app
COPY api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

$ docker build -f jar.Dockerfile -t my-api .
$ docker run -p 9000:8080 -it --rm my-api # running as frontground
# check URI http://localhost:9000/books would return json as api.jar behavior  
# ctrl-c to kill container
```

## Using Dockerfile to build image and run .war program

```bash
$ ls web.war                                    
web.war
$ vi war.Dockerfile
FROM tomcat:10
COPY web.war ${CATALINA_HOME}/webapps/ROOT.war
EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]

$ docker build -f war.Dockerfile -t my-web-app .
$ docker run -p 9001:8080 -it --rm my-web-app 
# check URI http://localhost:90001 would show web page  
# ctrl-c to kill container
```
## Using Dockerfile to build maven project and image, then run it

```bash
$ cd api
$ ls
HELP.md          gradle           gradlew.bat      mvnw             pom.xml          src
build.gradle     gradlew          mvnw.cmd         settings.gradle
$ vi maven.Dockerfile
FROM maven:3.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src src
RUN mvn package
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/api.jar"]

$ docker build -f maven.Dockerfile -t my-api-maven .
$ docker run -p 9010:8080 -it --rm my-api-maven
# ctrl-c to kill container
```

## Using Dockerfile to build gradle project and image, then run it

```bash
$ cd api
$ ls
HELP.md          gradle           gradlew.bat      mvnw             pom.xml          src
build.gradle     gradlew          mvnw.cmd         settings.gradle
$ vi gradle.Dockerfile

FROM gradle:8.0-jdk17
WORKDIR /app
RUN chown -R gradle:gradle /app
USER gradle
COPY build.gradle .
COPY src src
RUN gradle build
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/api.jar"]

$ docker build -f gradle.Dockerfile -t my-api-gradle .
$ docker run -p 9011:8080 -it --rm my-api-gradle
# using other tab/terminal to run `docker volume ls` to check used volume
# ctrl-c to kill container
```

## Using Multi-stage Builds

```bash
$ cd web

#
# Maven
# 
$ vi maven-multi.Dockerfile
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

$ docker build -f maven-multi.Dockerfile -t my-web-maven-multi .
$ docker run -p 9020:8080 -it --rm -t my-web-maven-multi

#
# Gradle
#
$ vi gradle-multi.Dockerfile
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

$ docker build -f gradle-multi.Dockerfile -t my-web-gradle-multi .
$ docker run -p 9021:8080 -it --rm -t my-web-gradle-multi
```

## Using BuildKit Cache Mount for Maven Dependencies

```bash
$ cd web
$ vi maven-cache.Dockerfile
FROM maven:3.9-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM tomcat:10
COPY --from=build /app/target/web.war ${CATALINA_HOME}/webapps/ROOT.war
EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]

$ docker build -f maven-cache.Dockerfile -t my-web-maven-cache .
$ vi pom.xml
# Updated h2database version from .214 to .212
$ docker build -f maven-cache.Dockerfile -t my-web-maven-cache .
# At this time, it only download updated packages from mvn package command
``` 

## Ref.

1. "Docker Memo | 楓鳴樂居 - Bill's Blog"
http://blog.ychobilllab.click/2023/04/docker-memo.html
