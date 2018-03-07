# requires Docker version 17.05.0-ce-rc1, build 2878a85
FROM openjdk:alpine

COPY  target/*.jar /maven/

CMD java $JAVA_OPTS -jar maven/*.jar