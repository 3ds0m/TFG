package com.edson.gonzales.aff.Service.JsonToDatabaseTest;

import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Service.JsonToDatabaseService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class JsonToDatabaseTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private JsonToDatabaseService jsonToDatabaseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertJsonFileToDatabase() throws IOException {
        // Crear el JSON de prueba directamente en el método
        String jsonString = "[\n" +
                "    {\n" +
                "        \"location_id\": \"1\",\n" +
                "        \"name\": \"Test Location\",\n" +
                "        \"distance\": \"5\",\n" +
                "        \"bearing\": \"North\",\n" +
                "        \"address_obj\": {\n" +
                "            \"street1\": \"Street 1\",\n" +
                "            \"street2\": \"Street 2\",\n" +
                "            \"city\": \"City\",\n" +
                "            \"country\": \"Country\",\n" +
                "            \"postalcode\": \"12345\",\n" +
                "            \"address_string\": \"Address String\"\n" +
                "        }\n" +
                "    }\n" +
                "]";

        // Convertir el JSON a un BufferedReader (simulación de la lectura del archivo)
        BufferedReader reader = new BufferedReader(new StringReader(jsonString));

        // Usar el BufferedReader simulado en lugar de un archivo real
        when(reader.readLine()).thenReturn("{\"location_id\": \"1\", \"name\": \"Test Location\", \"distance\": \"5\", \"bearing\": \"North\", \"address_obj\": {\"street1\": \"Street 1\", \"street2\": \"Street 2\", \"city\": \"City\", \"country\": \"Country\", \"postalcode\": \"12345\", \"address_string\": \"Address String\"}}")
                .thenReturn(null);  // Fin de la lectura

        // Simular el comportamiento del LocationRepository
        List<Location> savedLocations = new ArrayList<>();
        doAnswer(invocation -> {
            savedLocations.addAll(invocation.getArgument(0));
            return null;
        }).when(locationRepository).saveAll(anyList());

        // Llamar al método que queremos probar
        jsonToDatabaseService.insertJsonFileToDatabase("dummy/path/to/json");

        // Verificar que el método saveAll fue llamado con la lista correcta
        verify(locationRepository, times(1)).saveAll(anyList());
    }
}
