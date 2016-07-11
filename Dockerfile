FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/ca-wiki.jar /ca-wiki/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/ca-wiki/app.jar"]
