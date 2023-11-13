# Use amazon JDK correto version 20
FROM amazoncorretto:20.0.2 as build

# Copy jar application to the image
COPY target/wex-purchases-control-*.jar wex-purchases-control.jar

# run application
ENTRYPOINT ["java", "-jar", "/wex-purchases-control.jar"]
