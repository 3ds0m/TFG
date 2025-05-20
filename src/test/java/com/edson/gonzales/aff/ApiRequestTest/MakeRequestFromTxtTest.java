//package com.edson.gonzales.aff.ApiRequestTest;
//
//import com.edson.gonzales.aff.Service.ApiRequestService;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MakeRequestFromTxtTest {
//
//    private final ApiRequestService service = new ApiRequestService();
//    private MockWebServer mockWebServer;
//    private File tempDir;
//    private File inputFile;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        // Set up MockWebServer
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        // Create temp directory for test output
//        tempDir = Files.createTempDirectory("api_test_output").toFile();
//        tempDir.deleteOnExit();
//
//        // Create temp input file
//        inputFile = File.createTempFile("urls_", ".txt");
//        inputFile.deleteOnExit();
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        // Clean up resources
//        mockWebServer.shutdown();
//
//        // Delete any remaining files in temp directory
//        if (tempDir.exists()) {
//            File[] files = tempDir.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    file.delete();
//                }
//            }
//            tempDir.delete();
//        }
//
//        // Delete input file if it still exists
//        if (inputFile.exists()) {
//            inputFile.delete();
//        }
//    }
//
//    @Test
//    void shouldMakeRequestsFromTxtAndSaveResponses() throws IOException {
//        // Prepare mock responses - one for each URL in the test
//        String jsonResponse = "{ \"data\": [ { \"location_id\": \"1\", \"name\": \"Lugar A\" } ] }";
//
//        // Queue up two responses for the two URLs we'll request
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody(jsonResponse)
//                .addHeader("Content-Type", "application/json"));
//
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody(jsonResponse)
//                .addHeader("Content-Type", "application/json"));
//
//        // Get URL paths from mock server
//        String url1 = mockWebServer.url("/test1").toString();
//        String url2 = mockWebServer.url("/test2").toString();
//
//        // Write URLs to the input file
//        try (FileWriter writer = new FileWriter(inputFile)) {
//            writer.write(url1 + "\n");
//            writer.write(url2);
//        }
//
//        // Create output directory path
//        String outputDirPath = tempDir.getAbsolutePath() + File.separator;
//
//        // Execute the method under test with increased timeouts
//        service.makeRequestsFromTxt(inputFile.getAbsolutePath(), outputDirPath);
//
//        // Verify JSON files were created
//        File[] files = tempDir.listFiles((dir, name) -> name.startsWith("response_"));
//        assertNotNull(files, "Output files should not be null");
//        assertEquals(2, files.length, "Should have created 2 output files");
//
//        // Verify content of the first file
//        if (files.length > 0) {
//            String content = Files.readString(files[0].toPath());
//            assertTrue(content.contains("\"location_id\": \"1\""),
//                    "Response file should contain the expected location_id");
//            assertTrue(content.contains("\"name\": \"Lugar A\""),
//                    "Response file should contain the expected name");
//        }
//    }
//
//    @Test
//    void shouldHandleEmptyFileWithoutErrors() throws IOException {
//        // Write nothing to the input file (empty)
//        try (FileWriter writer = new FileWriter(inputFile)) {
//            // File is intentionally left empty
//        }
//
//        String outputDirPath = tempDir.getAbsolutePath() + File.separator;
//
//        // Call the method with an empty file
//        service.makeRequestsFromTxt(inputFile.getAbsolutePath(), outputDirPath);
//
//        // Verify no response files were created
//        File[] files = tempDir.listFiles((dir, name) -> name.startsWith("response_"));
//        if (files == null) {
//            files = new File[0]; // Handle null case for assertion
//        }
//        assertEquals(0, files.length, "No files should be created for empty input");
//    }
//}