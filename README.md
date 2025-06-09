  # Despliegue del Backend en Render

El backend de este proyecto fue desarrollado con **Spring Boot** y desplegado en la plataforma [Render](https://render.com) usando un contenedor Docker personalizado. El backend se conecta a una base de datos PostgreSQL y utiliza la API de Stripe para procesar pagos.

---

## Tecnologías utilizadas

- Spring Boot
- PostgreSQL (Render)
- Stripe (API Key privada)
- Docker
- Render (Web Service)

---
## Despliegue de la Base de Datos PostgreSQL
Creé una nueva base de datos PostgreSQL desde el panel de Render.

Seleccioné la región Frankfurt.

Render generó automáticamente las credenciales de conexión:

- Hostname: dpg-d0m2r09r0fns73c8sqrg-a.frankfurt-postgres.render.com  
- Port: 5432  
- Database: db  
- Username: user  
- Password: ****  
- Exporté mi base de datos local con:

    - pg_dump -U postgres -d nombre_local -f data.sql

- Importé el dump a la base de datos en Render con:

  - PGPASSWORD=asOBqXTKdc4oElaxi9Q1OAzqPmat8WD1 \
  psql -h dpg-d0m2r09r0fns73c8sqrg-a.frankfurt-postgres.render.com \
  -U user -d db -f data.sql
---

## Dockerfile utilizado

Empleé un `Dockerfile` multietapa para compilar y ejecutar la aplicación de forma optimizada:

```Dockerfile```
---

#### FROM maven:3.9-eclipse-temurin-21-alpine AS build

##### WORKDIR /app
##### COPY pom.xml .
##### COPY mvnw .
##### COPY .mvn .mvn
##### RUN mvn dependency:go-offline -B
##### COPY src src
##### RUN mvn package -DskipTests

##### FROM eclipse-temurin:21-jre-alpine
##### WORKDIR /app
##### COPY --from=build /app/target/*.jar app.jar

##### RUN addgroup --system javauser && adduser --system --ingroup javauser javauser
#### RUN chown -R javauser:javauser /app
##### USER javauser


##### EXPOSE 8080
##### CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]

### Explicacion: 
- Usa una imagen de Java basada en Eclipse Temurin para compilar y ejecutar una aplicación Spring Boot.
- Primero, construye el proyecto con Maven en una etapa separada, generando el archivo .jar.
- Luego, copia ese .jar a una imagen más ligera solo con el entorno de ejecución.
- Finalmente, expone el puerto 8080 y lanza la aplicación de forma segura con un usuario no root.

---
## Configuración del Backend en Render
- Inicié sesión en Render y seleccioné New Web Service.

- Elegí el [repositorio](https://github.com/3ds0m/TFG) desde GitHub.

- Seleccioné Docker como tipo de despliegue.

- Render detectó y utilizó automáticamente el Dockerfile.
---
##  Variables de entorno configuradas
En el panel de Render, añadí las siguientes variables:

- PORT = ${PORT:8080}
- STRIPE_API= ${STRIPE_API}
- SPRING.DATASOURCE.PASSWORD= ${SPRING.DATASOURCE.PASSWORD}
---
## Configuración del application.properties

- spring.application.name=Aff
- server.port=${PORT:8080}

- spring.datasource.url=jdbc:postgresql://<host>:5432/aff_db?charSet=UTF8
- spring.datasource.username=aff_user
- spring.datasource.password=${SPRING.DATASOURCE.PASSWORD}
- spring.datasource.driver-class-name=org.postgresql.Driver

- spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.show-sql=true

- stripe.api=${STRIPE.API}

---
## Resultado
La aplicación se desplegó correctamente. Render expuso el backend en el puerto 8080 y la API quedó disponible públicamente para ser consumida por el frontend.
