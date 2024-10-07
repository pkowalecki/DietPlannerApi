FROM amazoncorretto:17
LABEL authors="Piotrek"
ADD target/DietPlannerRestApi-0.0.1-SNAPSHOT.jar DietPlannerRestApi-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "DietPlannerRestApi-0.0.1-SNAPSHOT.jar"]