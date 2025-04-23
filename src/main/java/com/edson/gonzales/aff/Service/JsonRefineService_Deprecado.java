package com.edson.gonzales.aff.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonRefineService_Deprecado {
    private static final String INPUT_JSON_PATH = "C:\\Users\\Edson\\Desktop\\Aff\\JSON\\combined_results.json";
    private static final String OUTPUT_JSON_PATH = "C:\\Users\\Edson\\Desktop\\Aff\\JSON\\updated_results.json";
    private static final String API_KEY = "746F3ABE44B944CDA5DCFF366DDFD396";
    private static final String API_DETAILS_URL = "https://api.content.tripadvisor.com/api/v1/location/%s/details?key=" + API_KEY + "&language=en&currency=USD";
    private static final String API_PHOTOS_URL = "https://api.content.tripadvisor.com/api/v1/location/%s/photos?key=" + API_KEY + "&language=en&limit=1&offset=1&source=Expert";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String processJson() {
        File inputFile = new File(INPUT_JSON_PATH);
        try {
            // 1. Procesar el JSON (tu código actual)
            JsonNode root = objectMapper.readTree(inputFile);

            for (JsonNode node : root) {
                if (node.has("location_id")) {
                    String locationId = node.get("location_id").asText();
                    System.out.println(locationId);

                    // Obtener los detalles (rating, price_level, cuisine)
                    JsonNode apiResponse = fetchDetailsFromApi(String.format(API_DETAILS_URL, locationId));

                    String rating = apiResponse.has("rating") ? apiResponse.get("rating").asText() : "N/A";
                    String priceLevel = apiResponse.has("price_level") ? apiResponse.get("price_level").asText() : "N/A";

                    // Extraer los tipos de cocina
                    String cuisineTypes = "";
                    if (apiResponse.has("cuisine")) {
                        for (JsonNode cuisine : apiResponse.get("cuisine")) {
                            if (cuisineTypes.length() > 0) {
                                cuisineTypes += ", ";
                            }
                            cuisineTypes += cuisine.get("localized_name").asText();
                        }
                    }

                    String imageUrl = fetchImageUrlFromApi(String.format(API_PHOTOS_URL, locationId));

                    // Añadir la información al JSON
                    ((ObjectNode) node).put("rating", rating);
                    ((ObjectNode) node).put("price_level", priceLevel);
                    ((ObjectNode) node).put("cuisine_types", cuisineTypes);
                    ((ObjectNode) node).put("image", imageUrl);
                }
            }

            // 2. Guardar el nuevo archivo
            File outputFile = new File(OUTPUT_JSON_PATH);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);

            // 3. Eliminar el archivo original solo si el nuevo se creó correctamente
            if (outputFile.exists() && outputFile.length() > 0) {
                if (inputFile.delete()) {
                    return "\nJSON actualizado guardado y archivo original eliminado";
                } else {
                    System.out.println("No se pudo eliminar el archivo original: {}" + INPUT_JSON_PATH);
                    return "JSON actualizado guardado (pero no se pudo eliminar el original)";
                }
            } else {
                throw new IOException("El archivo de salida no se creó correctamente");
            }

        } catch (IOException e) {
            System.out.println("Error procesando JSON" + e);
            return "Error: " + e.getMessage();
        }
    }

    // Metodo que construye el link de solicitud HTTP para detalles de cocina, rating y precio
    private JsonNode fetchDetailsFromApi(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readTree(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para hacer solicitud HTTP y extraer la URL de la imagen
    private String fetchImageUrlFromApi(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonNode jsonResponse = objectMapper.readTree(response.body().string());
                // Verificar si existe "data" y si tiene al menos una imagen
                if (jsonResponse.has("data") && jsonResponse.get("data").isArray() && jsonResponse.get("data").size() > 0) {
                    JsonNode firstImage = jsonResponse.get("data").get(0);
                    if (firstImage.has("images") && firstImage.get("images").has("original")) {
                        return firstImage.get("images").get("original").get("url").asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}
