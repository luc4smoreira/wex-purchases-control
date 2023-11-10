# Use JDK 20
FROM openjdk:20-jdk as build

# Copie o jar do seu aplicativo para a imagem
COPY target/wex-purchases-control-*.jar wex-purchases-control.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/wex-purchases-control.jar"]
