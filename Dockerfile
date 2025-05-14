# Usa una imagen base de OpenJDK
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar generado a la carpeta /app dentro del contenedor
COPY target/*.jar /app/

# Expone el puerto donde la aplicación estará disponible
EXPOSE 9000

# Ejecuta el archivo .jar de la aplicación
ENTRYPOINT ["java", "-jar", "Aff-0.0.1-SNAPSHOT.jar"]
