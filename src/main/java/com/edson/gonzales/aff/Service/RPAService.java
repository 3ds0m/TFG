package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.DTO.LocationDTO;
import com.edson.gonzales.aff.Repository.LocationRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class RPAService {

    private final LocationRepository locationRepository;

    // Método para recolectar datos humanizados desde TripAdvisor
    public void completarDatosDesdeTripAdvisor() {
        // Configuración de WebDriver para Edge
        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();  // Usamos EdgeDriver

        // Obtener las ubicaciones con campos nulos
        List<LocationDTO> locations = locationRepository.findLocationsWhereExtraFieldsAreNull();

        // Procesar cada una de las ubicaciones
        for (LocationDTO dto : locations) {
            try {
                String locationId = dto.getLocationId();
                System.out.println("Accediendo a la ubicación con ID: " + locationId);
                driver.get("https://www.tripadvisor.com/" + locationId);  // Acceder directamente a la página de TripAdvisor
                Thread.sleep(5000);  // Espera para que cargue la página

                // Humanizar la interacción
                humanizarInteraccion(driver);

                // Llamar al método para recolectar datos
                recolectarDatos(driver);

            } catch (Exception e) {
                System.out.println("Error procesando la ubicación con ID: " + dto.getLocationId() + " → " + e.getMessage());
            }
        }

        // Cerrar el driver de Edge después de procesar todas las ubicaciones
        driver.quit();
    }

    // Método para simular una interacción humana (movimiento del ratón, desplazamiento, clics aleatorios)
    private void humanizarInteraccion(WebDriver driver) throws InterruptedException {
        Actions actions = new Actions(driver);
        Random rand = new Random();

        // Simular un retraso aleatorio antes de empezar
        Thread.sleep(rand.nextInt(2000) + 1000);  // Espera aleatoria entre 1 y 3 segundos

        // Mover el ratón a un área aleatoria de la página
        WebElement randomElement = driver.findElement(By.cssSelector("body"));
        actions.moveToElement(randomElement).perform();
        Thread.sleep(rand.nextInt(2000) + 1000);  // Espera aleatoria entre 1 y 3 segundos

        // Desplazarse hacia abajo por la página
        actions.sendKeys(Keys.PAGE_DOWN).perform();
        Thread.sleep(rand.nextInt(3000) + 2000);  // Espera aleatoria entre 2 y 5 segundos

        // Hacer clic en un enlace aleatorio (como parte de la interacción humana)
        WebElement randomClickable = driver.findElement(By.cssSelector("a"));
        actions.moveToElement(randomClickable).click().perform();
        Thread.sleep(rand.nextInt(2000) + 1000);  // Espera aleatoria para simular un clic humano
    }

    // Método para recolectar los datos desde la página de TripAdvisor
    private void recolectarDatos(WebDriver driver) {
        try {
            // Recoger el número de teléfono
            WebElement phoneElement = driver.findElement(By.cssSelector("span.biGQs._P.XWJSj.Wb"));
            String phone = phoneElement.getText();
            System.out.println("Teléfono: " + phone);

            // Recoger el rating
            WebElement ratingElement = driver.findElement(By.cssSelector("div[data-automation='bubbleRatingValue']"));
            String rating = ratingElement.getText();
            System.out.println("Calificación: " + rating);

            // Recoger la cantidad de reseñas
            WebElement reviewsElement = driver.findElement(By.cssSelector("div[data-automation='bubbleReviewCount']"));
            String reviews = reviewsElement.getText();
            System.out.println("Número de reseñas: " + reviews);

            // Recoger el tipo de cocina (si es un restaurante)
            WebElement cuisineTypeElement = driver.findElement(By.cssSelector("span.biGQs._P.pZUbB.KxBGd"));
            String cuisineType = cuisineTypeElement.getText();
            System.out.println("Tipo de cocina: " + cuisineType);

            // Optional<Location> optionalLocation = locationRepository.findByLocationId(dto.getLocationId());
            // if (optionalLocation.isPresent()) {
            //    Location loc = optionalLocation.get();
            //    loc.setPhoneNumber(phone);
            //    loc.setRating(rating.isEmpty() ? null : Double.valueOf(rating.replaceAll("[^0-9.]", "")));
            //    loc.setReviewCount(reviews.isEmpty() ? null : Integer.parseInt(reviews.replaceAll("\\D+", "")));
            //    loc.setCuisine_type(cuisineType);
            //    locationRepository.save(loc);
            // }

        } catch (Exception e) {
            System.out.println("Error al recolectar datos: " + e.getMessage());
        }
    }
}
