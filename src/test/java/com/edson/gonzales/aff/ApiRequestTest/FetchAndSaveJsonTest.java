//package com.edson.gonzales.aff.ApiRequestTest;
//
//import com.edson.gonzales.aff.Service.ApiRequestService;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FetchAndSaveJsonTest {
//
//    private final ApiRequestService service = new ApiRequestService();
//
//    @Test
//    void shouldFetchJsonAndSaveToFile() throws IOException {
//        // Configurar servidor simulado
//        MockWebServer mockWebServer = new MockWebServer();
//        String jsonResponse = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A\" } ] }";
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody(jsonResponse)
//                .addHeader("Content-Type", "application/json"));
//        mockWebServer.start();
//
//        String url = mockWebServer.url("/test").toString();
//        File tempFile = File.createTempFile("response_test_", ".json");
//
//        // Ejecutar método a probar
//        service.fetchAndSaveJson(url, tempFile.getAbsolutePath());
//
//        // Verificar que el archivo fue creado y contiene el JSON correcto
//        String savedContent = Files.readString(tempFile.toPath());
//        assertTrue(savedContent.contains("Lugar A"));
//        assertTrue(savedContent.contains("\"location_id\": \"1\""));
//
//        // Limpieza
//        tempFile.delete();
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    void shouldNotSaveFileIfHttpError() throws IOException {
//        MockWebServer mockWebServer = new MockWebServer();
//        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
//        mockWebServer.start();
//
//        String url = mockWebServer.url("/notfound").toString();
//        File tempFile = File.createTempFile("response_error_", ".json");
//        tempFile.delete(); // Asegurarse de que esté limpio
//
//        service.fetchAndSaveJson(url, tempFile.getAbsolutePath());
//
//        // Verificar que el archivo no se haya creado o esté vacío
//        assertFalse(tempFile.exists() && tempFile.length() > 0);
//
//        mockWebServer.shutdown();
//    }
//}