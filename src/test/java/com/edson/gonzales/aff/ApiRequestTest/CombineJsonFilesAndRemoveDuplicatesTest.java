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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CombineJsonFilesAndRemoveDuplicatesTest {

    private final ApiRequestService service = new ApiRequestService();

    @Test
    void shouldCombineAndRemoveDuplicateLocationIds() throws IOException, JSONException {
        // Crear archivos temporales
        File file1 = File.createTempFile("dup1_", ".json");
        File file2 = File.createTempFile("dup2_", ".json");

        String json1 = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A\" }, { \"location_id\": \"2\", \"name\": \"Lugar B\" } ] }";
        String json2 = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A duplicado\" }, { \"location_id\": \"3\", \"name\": \"Lugar C\" } ] }";

        try (FileWriter w1 = new FileWriter(file1);
             FileWriter w2 = new FileWriter(file2)) {
            w1.write(json1);
            w2.write(json2);
        }

        // Archivo de salida
        File outputFile = File.createTempFile("combined_nodup_", ".json");

        service.combineJsonFilesAndRemoveDuplicates(
                List.of(file1.getAbsolutePath(), file2.getAbsolutePath()),
                outputFile.getAbsolutePath()
        );

        String result = Files.readString(outputFile.toPath());
        JSONObject wrapped = new JSONObject("{\"data\":" + result + "}");
        JSONArray dataArray = wrapped.getJSONArray("data");

        // Esperamos solo 3 objetos con location_id únicos
        assertEquals(3, dataArray.length());

        // Verifica que todos los location_ids sean únicos
        Set<String> locationIds = new HashSet<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject obj = dataArray.getJSONObject(i);
            locationIds.add(obj.getString("location_id"));
        }
        assertEquals(3, locationIds.size());

        file1.delete();
        file2.delete();
        outputFile.delete();
    }

    @Test
    void shouldHandleFileWithoutDataFieldGracefully() throws IOException, JSONException {
        File invalidFile = File.createTempFile("no_data_", ".json");
        try (FileWriter w = new FileWriter(invalidFile)) {
            w.write("{ \"meta\": \"no data section\" }");
        }

        File outputFile = File.createTempFile("combined_empty_", ".json");

        service.combineJsonFilesAndRemoveDuplicates(
                List.of(invalidFile.getAbsolutePath()),
                outputFile.getAbsolutePath()
        );

        String result = Files.readString(outputFile.toPath());
        JSONObject wrapped = new JSONObject("{\"data\":" + result + "}");
        JSONArray dataArray = wrapped.getJSONArray("data");

        assertEquals(0, dataArray.length());

        invalidFile.delete();
        outputFile.delete();
    }
}
