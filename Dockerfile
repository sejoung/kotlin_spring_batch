FROM eclipse-temurin:17.0.4.1_1-jre-jammy
COPY ./build/libs/kotlin_spring_batch_test-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -jar app.jar
