FROM openjdk:11 AS build
COPY . .

# change mvnw file ending
RUN apt-get update && apt-get install dos2unix
RUN dos2unix mvnw
RUN chmod +x mvnw
RUN ./mvnw clean install


FROM openjdk:11 AS deploy

COPY --from=build /target/bot-1.0.jar /app/bot.jar
WORKDIR /app

ENTRYPOINT [ "java", "-jar", "bot.jar"]