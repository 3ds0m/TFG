//package com.edson.gonzales.aff.ApiRequestTest;
//import com.edson.gonzales.aff.Service.ApiRequestService;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ReadUrlsFromFileTest {
//
//    private final ApiRequestService service = new ApiRequestService();
//
//    @Test
//    void shouldReadUrlsFromTextFile() throws IOException {
//        // Crear contenido de prueba
//        String content = "https://api.example.com/data1\nhttps://api.example.com/data2";
//        Path tempFile = Files.createTempFile("urls_test_", ".txt");
//        Files.write(tempFile, content.getBytes());
//
//        // Ejecutar el método
//        List<String> urls = service.readUrlsFromFile(tempFile.toString());
//
//        // Verificar resultados
//        assertEquals(2, urls.size());
//        assertEquals("https://api.example.com/data1", urls.get(0));
//        assertEquals("https://api.example.com/data2", urls.get(1));
//
//        // Eliminar archivo temporal
//        Files.deleteIfExists(tempFile);
//    }
//
//    @Test
//    void shouldReturnEmptyListForEmptyFile() throws IOException {
//        Path tempFile = Files.createTempFile("empty_urls_test_", ".txt");
//
//        List<String> urls = service.readUrlsFromFile(tempFile.toString());
//
//        assertNotNull(urls);
//        assertTrue(urls.isEmpty());
//
//        Files.deleteIfExists(tempFile);
//    }
//
//    @Test
//    void shouldThrowIOExceptionForInvalidPath() {
//        String invalidPath = "nonexistent_file.txt";
//
//        assertThrows(IOException.class, () -> {
//            service.readUrlsFromFile(invalidPath);
//        });
//    }
//}
