FROM openjdk:17-oracle
ADD /target/demo-0.0.1-SNAPSHOT.jar stinder.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","stinder.jar"]