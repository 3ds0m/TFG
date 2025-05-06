package com.edson.gonzales.aff.JsonRefineTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edson.gonzales.aff.Service.JsonRefineService;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JsonRefineTest {

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private ObjectMapper mockObjectMapper;

    @InjectMocks
    private JsonRefineService jsonRefineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }
}
