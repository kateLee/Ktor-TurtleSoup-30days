FROM openjdk:8-jre-alpine
EXPOSE 8080:8080
RUN mkdir /app

# Rootless containers
ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER
RUN chown -R $APPLICATION_USER /app
USER $APPLICATION_USER

COPY ./build/install/ktor-turtlesoup/ /app/
WORKDIR /app/bin
CMD ["./ktor-turtlesoup"]
