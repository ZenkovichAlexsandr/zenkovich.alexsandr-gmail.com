FROM java:8-jre
MAINTAINER Alexander Zenkovich <zenkovich.alexsandr@gmail.com>

WORKDIR /home/stub

COPY ./build/libs/api*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8081