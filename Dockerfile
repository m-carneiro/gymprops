FROM openjdk:latest
EXPOSE 7777
WORKDIR /app
RUN mkdir -p /app/
ADD build/libs/gymprops-1.jar /app/gymprops-1.jar
ENTRYPOINT ["java", "-jar", "gymprops-1.jar"]