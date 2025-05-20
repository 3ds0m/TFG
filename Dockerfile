# Usa una imagen base de OpenJDK
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar generado al contenedor
COPY target/*.jar app.jar

# Expone el puerto (Render lo ignora, pero es buena práctica)
EXPOSE 8080

# Usa la variable PORT que Render inyecta
ENTRYPOINT ["java", "-jar", "app.jar"]
