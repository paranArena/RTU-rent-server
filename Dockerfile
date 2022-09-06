FROM adoptopenjdk/openjdk11
ENV APP_HOME=/usr/app
WORKDIR ${APP_HOME}
COPY build/libs/application.war application.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.war"]