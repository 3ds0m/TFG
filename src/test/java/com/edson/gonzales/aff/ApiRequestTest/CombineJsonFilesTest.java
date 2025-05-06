package com.edson.gonzales.aff.ApiRequestTest;

import com.edson.gonzales.aff.Service.ApiRequestService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CombineJsonFilesTest {

    private final ApiRequestService service = new ApiRequestService();

    @Test
    void shouldCombineJsonFilesWithDataArrays() throws IOException, JSONException {
        // Crear archivos JSON temporales con datos
        File file1 = File.createTempFile("test1_", ".json");
        File file2 = File.createTempFile("test2_", ".json");

        String json1 = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A\" } ] }";
        String json2 = "{ \"data\": [ { \"location_id\": \"2\", \"name\": \"Lugar B\" } ] }";

        try (FileWriter writer1 = new FileWriter(file1);
             FileWriter writer2 = new FileWriter(file2)) {
            writer1.write(json1);
            writer2.write(json2);
        }

        // Archivo combinado
        File combinedFile = File.createTempFile("combined_", ".json");

        // Ejecutar método
        service.combineJsonFiles(List.of(file1.getAbsolutePath(), file2.getAbsolutePath()), combinedFile.getAbsolutePath());

        // Verificar contenido combinado
        String combinedContent = Files.readString(combinedFile.toPath());
        JSONObject resultJson = new JSONObject("{\"data\":" + combinedContent + "}");
        JSONArray combinedArray = resultJson.getJSONArray("data");

        assertEquals(2, combinedArray.length());
        assertEquals("1", combinedArray.getJSONObject(0).getString("location_id"));
        assertEquals("2", combinedArray.getJSONObject(1).getString("location_id"));

        // Limpieza
        file1.delete();
        file2.delete();
        combinedFile.delete();
    }

    @Test
    void shouldIgnoreFilesWithoutDataKey() throws IOException, JSONException {
        File validFile = File.createTempFile("valid_", ".json");
        File invalidFile = File.createTempFile("invalid_", ".json");

        String validJson = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A\" } ] }";
        String invalidJson = "{ \"error\": \"no data here\" }";

        try (FileWriter writer1 = new FileWriter(validFile); FileWriter writer2 = new FileWriter(invalidFile)) {
            writer1.write(validJson);
            writer2.write(invalidJson);
        }

        File outputFile = File.createTempFile("combined_valid_only_", ".json");
        service.combineJsonFiles(List.of(validFile.getAbsolutePath(), invalidFile.getAbsolutePath()), outputFile.getAbsolutePath());

        String content = Files.readString(outputFile.toPath());
        JSONObject resultJson = new JSONObject("{\"data\":" + content + "}");
        JSONArray array = resultJson.getJSONArray("data");

        assertEquals(1, array.length());
        assertEquals("1", array.getJSONObject(0).getString("location_id"));

        validFile.delete();
        invalidFile.delete();
        outputFile.delete();
    }
}