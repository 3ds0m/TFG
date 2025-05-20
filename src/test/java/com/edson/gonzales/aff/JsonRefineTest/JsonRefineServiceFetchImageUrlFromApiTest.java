//package com.edson.gonzales.aff.JsonRefineTest;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.edson.gonzales.aff.Service.JsonRefineService;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static org.mockito.Mockito.*;
//
//public class JsonRefineServiceFetchImageUrlFromApiTest {
//
//    @Mock
//    private OkHttpClient mockClient;
//
//    @Mock
//    private ObjectMapper mockObjectMapper;
//
//    @InjectMocks
//    private JsonRefineService jsonRefineService;
//
//    @Test
//    public void testFetchImageUrlFromApi() throws Exception {
//        // Simulamos la respuesta de la API de imagen
//        String mockImageUrlResponse = "{\"data\":[{\"images\":{\"original\":{\"url\":\"http://example.com/image.jpg\"}}}]}";
//
//        JsonNode mockResponse = mock(ObjectMapper.class).readTree(mockImageUrlResponse);
//        when(mockClient.newCall(any(Request.class)).execute()).thenReturn(mock(Response.class));
//        when(mockObjectMapper.readTree(mockImageUrlResponse)).thenReturn(mockResponse);
//
//        // Ejecutamos el método
//        String imageUrl = jsonRefineService.fetchImageUrlFromApi("https://api.example.com/photos");
//
//        // Verificamos que la URL de la imagen sea la esperada
//        assert(imageUrl.equals("http://example.com/image.jpg"));
//    }
//}
