FROM amazoncorretto:11 AS build
COPY orchestrator_service/src /nasko_admin_tool/orchestrator_service/src/
ADD /orchestrator_service/orchestrator_service-0.0.1-SNAPSHOT.jar springboot-nasko.jar
COPY pom.xml /nasko_admin_tool/pom.xml/
EXPOSE 8080
ENTRYPOINT ["java","-jar","springboot-nasko.jar"]