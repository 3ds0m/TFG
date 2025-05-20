/*package com.edson.gonzales.aff.MapTest;

import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Service.MapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private MapService mapService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateLatLonForIncompleteLocations() {
        Location location1 = Location.builder().build();
        location1.setLocationId("1L");
        location1.setAddressString("Calle Falsa 123");

        Location location2 = Location.builder().build();
        location2.setLocationId("2L");
        location2.setAddressString("Avenida Siempre Viva 742");

        when(locationRepository.findIncompleteLocations()).thenReturn(Arrays.asList(location1, location2));
        when(locationRepository.save(any(Location.class))).thenReturn(location1);

        // Simulamos que la respuesta de las coordenadas sea correcta
        MapService.Coordinates mockCoordinates = new MapService.Coordinates(40.7128, -74.0060);
        try {
            // Mock del método getCoordinatesFromAddress para que devuelva las coordenadas simuladas
            when(mapService.getCoordinatesFromAddress("Calle Falsa 123, null, null, null")).thenReturn(Optional.of(mockCoordinates));
            when(mapService.getCoordinatesFromAddress("Avenida Siempre Viva 742, null, null, null")).thenReturn(Optional.of(mockCoordinates));

            mapService.updateLatLonForIncompleteLocations();

            // Verificamos que el método save fue llamado dos veces, una por cada Location
            verify(locationRepository, times(2)).save(any(Location.class));
        } catch (Exception e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testGetCoordinatesFromAddress_Success() throws Exception {
        String address = "Calle Falsa 123, Ciudad, País";

        // Simulamos la respuesta del API
        MapService.Coordinates coordinates = new MapService.Coordinates(40.7128, -74.0060);

        Optional<MapService.Coordinates> result = mapService.getCoordinatesFromAddress(address);

        assertTrue(result.isPresent());
        assertEquals(40.7128, result.get().latitude);
        assertEquals(-74.0060, result.get().longitude);
    }

    @Test
    void testGetCoordinatesFromAddress_NotFound() throws Exception {
        String address = "Dirección que no existe, Ciudad, País";

        Optional<MapService.Coordinates> result = mapService.getCoordinatesFromAddress(address);

        assertFalse(result.isPresent());
    }
}*/
