FROM amazoncorretto:17.0.6 as BUILD
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM amazoncorretto:17.0.6 as RUN 
WORKDIR /app
COPY --from=BUILD /app/build/libs/AuthTestApp-1.0.jar .

CMD ["java", "-jar", "AuthTestApp-1.0.jar"]
