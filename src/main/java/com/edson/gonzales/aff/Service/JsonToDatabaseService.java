package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;
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

            Location location = Location.builder().build();

            location.setLocationId(jsonObject.getString("location_id"));
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
            }
            // Campos adicionales que vendrán después pueden ir aquí como null
            location.setImage(null);  // Ejemplo de campo que será llenado por el RPA
            location.setPhoneNumber(null);
            location.setRating(null);
            location.setReviewCount(null);
            location.setCuisine_type(null);
            location.setPriceLevel(null);

            locations.add(location);
        }

        locationRepository.saveAll(locations);
        System.out.println("✅ Datos insertados correctamente en la base de datos.");
    }
}
