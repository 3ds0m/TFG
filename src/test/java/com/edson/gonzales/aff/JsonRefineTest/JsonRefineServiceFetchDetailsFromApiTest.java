package com.edson.gonzales.aff.JsonRefineTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.edson.gonzales.aff.Service.JsonRefineService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class JsonRefineServiceFetchDetailsFromApiTest {

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private ObjectMapper mockObjectMapper;

    @InjectMocks
    private JsonRefineService jsonRefineService;

    @Test
    public void testFetchDetailsFromApi() throws Exception {
        // Simulamos la respuesta de la API
        String mockApiResponse = "{\"rating\":\"4.5\", \"phone\":\"123-456-7890\", \"num_reviews\":\"100\", \"price_level\":\"2\", \"cuisine\":[{\"localized_name\":\"Italian\"}]}";

        JsonNode mockResponse = mock(ObjectMapper.class).readTree(mockApiResponse);
        when(mockClient.newCall(any(Request.class)).execute()).thenReturn(mock(Response.class));
        when(mockObjectMapper.readTree(mockApiResponse)).thenReturn(mockResponse);

        // Ejecutamos el método
        JsonNode result = jsonRefineService.fetchDetailsFromApi("https://api.example.com");

        // Verificamos que la respuesta fue procesada correctamente
        assert(result != null);
    }
}
