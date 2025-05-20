package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JsonToDatabaseService {
    @Autowired
    private LocationRepository locationRepository;
    //Crea un archivo nuevo cogiendo el archivo json que encuentra en el path que se para por parametros
    //Construye un nuevo json con todo esto
    @Transactional
    public void insertJsonFileToDatabase(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();

        JSONArray dataArray = new JSONArray(jsonBuilder.toString());
        List<Location> locations = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject jsonObject = dataArray.getJSONObject(i);

            String locationId = jsonObject.getString("location_id");

            // Verificar si el ID ya existe en la base de datos
            if (!locationRepository.existsByLocationId(locationId)) {
                Location location = Location.builder().build();
                location.setLocationId(locationId);
                location.setName(jsonObject.optString("name", null));
                location.setDistance(jsonObject.optString("distance", null));
                location.setBearing(jsonObject.optString("bearing", null));

                JSONObject addressObj = jsonObject.optJSONObject("address_obj");
                if (addressObj != null) {
                    location.setStreet1(addressObj.optString("street1", null));
                    location.setStreet2(addressObj.optString("street2", null));
                    location.setCity(addressObj.optString("city", null));
                    location.setCountry(addressObj.optString("country", null));
                    location.setPostalcode(addressObj.optString("postalcode", null));
                    location.setAddressString(addressObj.optString("address_string", null));
                    location.setImage(jsonObject.optString("image", null));
                    location.setPhoneNumber(jsonObject.optString("phone", null));
                    String ratingStr = jsonObject.optString("rating", "0");
                    location.setRating(ratingStr.matches("-?\\d+(\\.\\d+)?") ? Double.valueOf(ratingStr) : 0.0);
                    location.setReviewCount(
                            jsonObject.has("num_reviews") && !jsonObject.optString("num_reviews", "").isEmpty()
                                    ? Integer.valueOf(jsonObject.optString("num_reviews", "0"))
                                    : 0
                    );
                    location.setCuisine_type(jsonObject.optString("cuisine_types", null));
                    location.setPriceLevel(jsonObject.optString("price_level", null));
                }

                location.setLatitude(null);
                location.setLongitude(null);

                locations.add(location);
                System.out.println("✅ Nuevo lugar agregado: " + locationId);
            } else {
                System.out.println("⚠️ Lugar ya existe en la base de datos: " + locationId);
            }
        }
// Solo guarda los nuevos registros
        locationRepository.saveAll(locations);
        System.out.println("✅ Datos actualizados correctamente en la base de datos.");
    }
}
