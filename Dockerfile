# Use amazon JDK correto version 20
FROM maven:3.9-amazoncorretto-20 as build

# Copy jar application to the image
COPY target/wex-purchases-control-*.jar wex-purchases-control.jar

# run application
ENTRYPOINT ["java", "-jar", "/wex-purchases-control.jar"]
