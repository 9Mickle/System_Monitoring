FROM adoptopenjdk:11-jre-hotspot
ENV APP_HOME=usr/app
WORKDIR ${APP_HOME}
COPY /target/*.jar app.jar
CMD ["java","-jar","app.jar"]
EXPOSE 8080
