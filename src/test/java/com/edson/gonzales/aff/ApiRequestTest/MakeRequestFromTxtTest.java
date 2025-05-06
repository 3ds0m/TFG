package com.edson.gonzales.aff.ApiRequestTest;

import com.edson.gonzales.aff.Service.ApiRequestService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class MakeRequestFromTxtTest {

    private final ApiRequestService service = new ApiRequestService();

    @Test
    void shouldMakeRequestsFromTxtAndSaveResponses() throws IOException {
        // Configurar MockWebServer
        MockWebServer mockWebServer = new MockWebServer();
        String jsonResponse = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A\" } ] }";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));
        mockWebServer.start();

        String url1 = mockWebServer.url("/test1").toString();
        String url2 = mockWebServer.url("/test2").toString();

        // Crear archivo de texto con URLs
        File inputFile = File.createTempFile("urls_", ".txt");
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(url1 + "\n");
            writer.write(url2);
        }

        // Directorio para las respuestas
        File outputDir = new File("temp_output_dir");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // Llamar al método que hace las solicitudes y guarda las respuestas
        service.makeRequestsFromTxt(inputFile.getAbsolutePath(), outputDir.getAbsolutePath());

        // Verificar que los archivos JSON fueron creados
        File[] files = outputDir.listFiles((dir, name) -> name.startsWith("response_"));
        assertNotNull(files);
        assertEquals(2, files.length);

        // Verificar el contenido de los archivos JSON
        for (File file : files) {
            String content = Files.readString(file.toPath());
            assertTrue(content.contains("Lugar A"));
            assertTrue(content.contains("\"location_id\": \"1\""));
        }

        // Limpiar archivos y directorios
        for (File file : files) {
            file.delete();
        }
        outputDir.delete();
        inputFile.delete();

        mockWebServer.shutdown();
    }

    @Test
    void shouldHandleEmptyFileWithoutErrors() throws IOException {
        // Crear un archivo de texto vacío
        File emptyFile = File.createTempFile("empty_urls_", ".txt");
        File outputDir = new File("temp_output_empty_dir");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // Llamar al método con un archivo vacío
        service.makeRequestsFromTxt(emptyFile.getAbsolutePath(), outputDir.getAbsolutePath());

        // Verificar que no se hayan creado archivos en el directorio de salida
        assertEquals(0, outputDir.listFiles().length);

        // Limpiar archivos y directorios
        emptyFile.delete();
        outputDir.delete();
    }
}
