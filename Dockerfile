FROM openjdk:11-jdk-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-cfg3v602i3mg6pauep6g-a.frankfurt-postgres.render.com:5432/tic_tac_toe_db_mtpw?user=tic_tac_toe_db_mtpw_user&password=WZIu93kTEEVC8bL0TUYOMVuwUWInXLwk
COPY target/tictactoe-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]