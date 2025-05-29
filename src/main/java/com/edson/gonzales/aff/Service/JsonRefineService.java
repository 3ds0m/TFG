package com.edson.gonzales.aff.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonRefineService {
    @Setter
    private String apiKey;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    //Recibe el la ruta donde esta el json general y sin campos repetidos
    //Lo convierte en un nodo y busca extrae el location id para construir un link
    //Con el link construido de detalles, espera la respuesta de fetchDetailsFromApi y extrae los campos indicados
    //Para cocina extrae sus distintos valores y los guarda todos en una sola linea como string
    //Crea el link de busqueda para fotos guarda el dato ya extraido en el metodo de fetchImageUrlFromApi
    //Aprovecha el archivo original mapeado como nodo y agrega los nuevos campos facilmente
    //Guarda el nuevo archivo en la misma ruta y lo llama updated_results
    public String processJson() {
        String INPUT_JSON_PATH = "./JSON/combined_results.json";
        String OUTPUT_JSON_PATH = "./JSON/updated_results.json";
        String API_KEY = "746F3ABE44B944CDA5DCFF366DDFD396";
        String API_DETAILS_URL = "https://api.content.tripadvisor.com/api/v1/location/%s/details?key=" + API_KEY + "&language=en&currency=USD";
        String API_PHOTOS_URL = "https://api.content.tripadvisor.com/api/v1/location/%s/photos?key=" + API_KEY + "&language=en&limit=1&offset=1&source=Expert";
        File inputFile = new File(INPUT_JSON_PATH);
        try {
            // 1. Procesar el JSON
            JsonNode root = objectMapper.readTree(inputFile);

            for (JsonNode node : root) {
                if (node.has("location_id")) {
                    String locationId = node.get("location_id").asText();
                    System.out.println(locationId);

                    // Obtener los detalles (rating, phone,num_reviews, price_level, cuisine e imageUrl)
                    JsonNode apiResponse = fetchDetailsFromApi(String.format(API_DETAILS_URL, locationId));

                    String rating = apiResponse.has("rating") ? apiResponse.get("rating").asText() : "N/A";
                    String phone = apiResponse.has("phone") ? apiResponse.get("phone").asText() : "N/A";
                    String num_reviews = apiResponse.has("num_reviews") ? apiResponse.get("num_reviews").asText() : "N/A";
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
                    ((ObjectNode) node).put("phone", phone);
                    ((ObjectNode) node).put("num_reviews", num_reviews);
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

    // Metodo auxiliar que devuelve una arbol con todo el contenido de detalles
    public JsonNode fetchDetailsFromApi(String url) {
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

    // Método auxiliar que busca el link de imagen dentro del json que recibe y lo devuelve, de no encontrarlo se usa NA
    public String fetchImageUrlFromApi(String url) {
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
