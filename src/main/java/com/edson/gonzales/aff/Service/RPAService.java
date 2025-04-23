package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.DTO.LocationDTO;
import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.AllArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.support.StandardServletPartUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RPAService {

    private final LocationRepository locationRepository;

    public void completarDatosDesdeGoogleMaps() {
        // Configuración de WebDriver para Edge
        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver(); // Usamos EdgeDriver

        // Obtener las ubicaciones con campos nulos
        List<LocationDTO> locations = locationRepository.findLocationsWhereExtraFieldsAreNull();

        // Procesar cada una de las ubicaciones
        for (LocationDTO dto : locations) {
            try {
                String address = dto.getName();
                String locationId = dto.getLocationId();
                System.out.println(address);
                driver.get("https://www.google.com/maps");
                Thread.sleep(2000);  // Espera para que cargue la página

                WebElement searchBox = driver.findElement(By.id("searchboxinput"));
                searchBox.clear();
                searchBox.sendKeys(address);
                searchBox.sendKeys(Keys.ENTER);  // Iniciar la búsqueda

                Thread.sleep(5000); // Esperar para que cargue el resultado

                // Variables para almacenar los datos extraídos
                String phone = "";
                String rating = "";
                String reviews = "";
                String cuisineType = "";
                // Intentar obtener la información de teléfono
                try {
                    WebElement phoneNumberElement = driver.findElement(By.cssSelector("div.Io6YTe.fontBodyMedium.kR99db.fdkmkc"));
                    phone= phoneNumberElement.getText();
                    System.out.println(phoneNumberElement);
                } catch (Exception ignored) {}

                // Intentar obtener el rating
                try {
                    WebElement ratingElement = driver.findElement(By.cssSelector("span.MW4etd"));
                    rating = ratingElement.getText();
                    System.out.println(ratingElement);
                } catch (Exception ignored) {}

                // Intentar obtener el número de reviews
                try {
                    WebElement reviewsElement = driver.findElement(By.xpath("//span[@aria-label='5,071 opiniones']"));
                    reviews = reviewsElement.getText();
                    System.out.println(reviewsElement);
                } catch (Exception ignored) {}
                // Intentar obtener el tipo de lugar (cuisine type)
                try {
                    WebElement cuisineTypeElement = driver.findElement(By.xpath("//button[contains(@class, 'DkEaL')]"));
                    cuisineType = cuisineTypeElement.getText();
                    System.out.println(cuisineTypeElement);
                } catch (Exception ignored) {}

                // Actualizar la base de datos con los nuevos datos
                Optional<Location> optionalLocation = locationRepository.findByLocationId(locationId);
                if (optionalLocation.isPresent()) {
                    Location loc = optionalLocation.get();
                    loc.setPhoneNumber(phone);
                    loc.setRating(rating.isEmpty() ? null : Double.valueOf(rating));
                    loc.setReviewCount(reviews.isEmpty() ? null : Integer.parseInt(reviews.replaceAll("\\D+", "")));
                    loc.setCuisine_type(cuisineType);
                    locationRepository.save(loc);
                }

            } catch (Exception e) {
                System.out.println("Error procesando: " + dto.getLocationId() + " → " + e.getMessage());
            }
        }

        // Cerrar el driver de Edge después de procesar todas las ubicaciones
        driver.quit();
    }
}
