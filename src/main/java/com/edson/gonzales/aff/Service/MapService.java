package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapService {
    private final LocationRepository locationRepository;
    public void updateLatLonForIncompleteLocations() {
        List<Location> locations = locationRepository.findIncompleteLocations();

        for (Location loc : locations) {
            try {
                // Reemplazamos el uso de String.format con la concatenación correcta
                // Asegúrate de que los campos street1, street2, city, country estén disponibles y sean correctos.
                String street1 = loc.getStreet1() != null ? loc.getStreet1() : "";
                String street2 = loc.getStreet2() != null ? loc.getStreet2() : "";
                String city = loc.getCity() != null ? loc.getCity() : "";
                String country = loc.getCountry() != null ? loc.getCountry() : "";

                // Concatenamos los campos de la dirección para formar la dirección completa
                String fullAddress = street1 + (street2.isEmpty() ? "" : ", " + street2)
                        + (city.isEmpty() ? "" : ", " + city)
                        + (country.isEmpty() ? "" : ", " + country);

                // Ahora que tenemos la dirección completa, obtenemos las coordenadas
                Optional<Coordinates> coordinates = getCoordinatesFromAddress(fullAddress);

                if (coordinates.isPresent()) {
                    loc.setLatitude(coordinates.get().latitude);
                    loc.setLongitude(coordinates.get().longitude);
                    locationRepository.save(loc);
                    System.out.println("Actualizada Location: " + loc.getLocationId());
                } else {
                    System.err.println("No se pudo obtener coordenadas para la ubicación " + loc.getLocationId());
                }

            } catch (Exception e) {
                System.err.println("Error al actualizar " + loc.getLocationId() + ": " + e.getMessage());
            }
        }
    }


    public Optional<Coordinates> getCoordinatesFromAddress(String address) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(address, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "TuApp/1.0 (tucorreo@ejemplo.com)")
                .header("Accept-Language", "es")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray jsonArray = new JSONArray(response.body());

        if (jsonArray.length() > 0) {
            JSONObject location = jsonArray.getJSONObject(0);
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            return Optional.of(new Coordinates(lat, lon));
        }
        return Optional.empty();
    }

    public static class Coordinates {
        public final Double latitude;
        public final Double longitude;

        public Coordinates(Double lat, Double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }
    }
}
