FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE_PATH
ARG JAR_FILE_NAME

COPY ${JAR_FILE_PATH} .

CMD ["java", "-jar", "${JAR_FILE_NAME}"]