FROM amazoncorretto:21.0.2

WORKDIR /app

ADD ./target/ask-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# 启动应用
CMD [ "java", "-jar", "app.jar"]
