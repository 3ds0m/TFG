package com.edson.gonzales.aff.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApiRequestService {

    // Método para leer las URLs desde un archivo de texto
    public List<String> readUrlsFromFile(String filePath) throws IOException {
        List<String> urls = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            urls.add(line);
        }
        br.close();
        return urls;
    }

    // Método para realizar la solicitud HTTP GET y guardar el resultado en un archivo
    public void fetchAndSaveJson(String urlString, String outputFilePath) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            System.out.println("Error en la solicitud: " + urlString + " Status: " + status);
            return;
        }

        // Leer la respuesta JSON
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Guardar el JSON en un archivo
        FileWriter file = new FileWriter(outputFilePath);
        file.write(response.toString());
        file.close();
        System.out.println("Guardado: " + outputFilePath);
    }

    // Método para combinar todos los archivos JSON en uno solo
    public void combineJsonFiles(List<String> jsonFiles, String outputFilePath) throws IOException {
        JSONArray combinedResults = new JSONArray();

        for (String jsonFile : jsonFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            try {
                JSONObject jsonObject = new JSONObject(content.toString());

                // Verificar si el campo "data" existe
                if (jsonObject.has("data")) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        combinedResults.put(data.get(i));
                    }
                } else {
                    // Si no existe el campo "data", agregar un mensaje de error o ignorar
                    System.out.println("No se encontró 'data' en: " + jsonFile);
                }
            } catch (Exception e) {
                // Si ocurre cualquier otro tipo de excepción, manejarla de manera adecuada
                System.out.println("Error procesando el archivo JSON: " + jsonFile + ". Error: " + e.getMessage());
            }
        }

        // Guardar el archivo combinado
        FileWriter file = new FileWriter(outputFilePath);
        file.write(combinedResults.toString(2));  // Formato bonito
        file.close();
        System.out.println("Archivo combinado guardado en: " + outputFilePath);
    }

    // Método para hacer todas las solicitudes desde el archivo y guardar las respuestas
    public void makeRequestsFromTxt(String inputFilePath, String outputDir) throws IOException {
        // Crear el directorio si no existe
        File outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        // Leer las URLs del archivo
        List<String> urls = readUrlsFromFile(inputFilePath);

        // Lista de archivos JSON generados
        List<String> jsonFiles = new ArrayList<>();

        // Realizar las solicitudes y guardar los resultados en archivos
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            String outputFilePath = outputDir + "response_" + i + ".json";
            fetchAndSaveJson(url, outputFilePath);
            jsonFiles.add(outputFilePath);
        }

        // Combinar todos los archivos JSON en uno solo
        String combinedFilePath = outputDir + "combined_results.json";
        combineJsonFiles(jsonFiles, combinedFilePath);
    }
    public void deleteJsonFilesInDirectory(String directoryPath, String fileToExclude) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                // Excluir el archivo combinado de la eliminación
                if (file.isFile() && file.getName().endsWith(".json") && !file.getName().equals(fileToExclude)) {
                    if (file.delete()) {
                        System.out.println("Archivo eliminado: " + file.getName());
                    } else {
                        System.out.println("Error al eliminar el archivo: " + file.getName());
                    }
                }
            }
        }
    }

    public void combineJsonFilesAndRemoveDuplicates(List<String> jsonFiles, String outputFilePath) throws IOException {
        // Usamos un Set para evitar duplicados basándonos en el location_id.
        // Para hacer esto necesitamos una forma de definir qué objeto es único.
        Set<String> uniqueLocationIds = new HashSet<>();
        JSONArray combinedResults = new JSONArray();

        for (String jsonFile : jsonFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            try {
                JSONObject jsonObject = new JSONObject(content.toString());

                // Verificar si el campo "data" existe
                if (jsonObject.has("data")) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        String locationId = item.getString("location_id");

                        // Si el location_id no ha sido procesado antes, lo agregamos
                        if (!uniqueLocationIds.contains(locationId)) {
                            combinedResults.put(item);
                            uniqueLocationIds.add(locationId);
                        }
                    }
                } else {
                    System.out.println("No se encontró 'data' en: " + jsonFile);
                }
            } catch (Exception e) {
                System.out.println("Error procesando el archivo JSON: " + jsonFile + ". Error: " + e.getMessage());
            }
        }

        // Guardar el archivo combinado sin duplicados
        FileWriter file = new FileWriter(outputFilePath);
        file.write(combinedResults.toString(2));  // Formato bonito
        file.close();
        System.out.println("Archivo combinado (sin duplicados) guardado en: " + outputFilePath);
    }
}
