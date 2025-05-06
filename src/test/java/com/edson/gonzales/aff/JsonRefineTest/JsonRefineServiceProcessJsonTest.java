package com.edson.gonzales.aff.JsonRefineTest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.edson.gonzales.aff.Service.JsonRefineService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class JsonRefineServiceProcessJsonTest {

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private ObjectMapper mockObjectMapper;

    @InjectMocks
    private JsonRefineService jsonRefineService;

    @Test
    public void testProcessJson() throws Exception {
        // Simulamos el contenido del JSON de entrada
        String mockJson = "[{\"location_id\": \"12345\"}]";
        // Simulamos las respuestas de la API
        String mockApiResponse = "{\"rating\":\"4.5\", \"phone\":\"123-456-7890\", \"num_reviews\":\"100\", \"price_level\":\"2\", \"cuisine\":[{\"localized_name\":\"Italian\"}]}";
        String mockImageUrlResponse = "{\"data\":[{\"images\":{\"original\":{\"url\":\"http://example.com/image.jpg\"}}}]}";

        JsonNode mockDetailsResponse = mock(ObjectMapper.class).readTree(mockApiResponse);
        JsonNode mockImageResponse = mock(ObjectMapper.class).readTree(mockImageUrlResponse);

        when(mockClient.newCall(any())).thenReturn((Call) mock(Response.class));
        when(mockObjectMapper.readTree(mockApiResponse)).thenReturn(mockDetailsResponse);
        when(mockObjectMapper.readTree(mockImageUrlResponse)).thenReturn(mockImageResponse);

        // Ejecutamos el método
        String result = jsonRefineService.processJson();

        // Verificamos que el método writeValue haya sido llamado para guardar el archivo de salida
        verify(mockObjectMapper, times(1)).writeValue((JsonGenerator) any(), any(JsonNode.class));

        // Verificamos que el resultado sea el esperado
        assert(result.contains("JSON actualizado guardado"));
    }
}
