package com.edson.gonzales.aff.ApiRequestTest;

import com.edson.gonzales.aff.Service.ApiRequestService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DeleteJsonInDirectoryTest {

    private final ApiRequestService service = new ApiRequestService();

    @Test
    void shouldDeleteJsonFilesExcludingSpecifiedOne() throws IOException {
        // Crear un directorio temporal
        File tempDir = new File("temp_test_dir");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        // Crear archivos JSON temporales
        File file1 = new File(tempDir, "test1.json");
        File file2 = new File(tempDir, "test2.json");
        File file3 = new File(tempDir, "test3.json");

        try (FileWriter writer1 = new FileWriter(file1);
             FileWriter writer2 = new FileWriter(file2);
             FileWriter writer3 = new FileWriter(file3)) {
            writer1.write("{ \"data\": [] }");
            writer2.write("{ \"data\": [] }");
            writer3.write("{ \"data\": [] }");
        }

        // Llamar al método de eliminación, excluyendo "test2.json"
        service.deleteJsonFilesInDirectory(tempDir.getAbsolutePath(), "test2.json");

        // Verificar que test2.json no haya sido eliminado
        assertTrue(file2.exists());

        // Verificar que los otros archivos sí hayan sido eliminados
        assertFalse(file1.exists());
        assertFalse(file3.exists());

        // Limpiar directorio
        file2.delete();
        tempDir.delete();
    }

    @Test
    void shouldNotDeleteAnyFilesIfDirectoryIsEmpty() {
        File emptyDir = new File("empty_dir");
        if (!emptyDir.exists()) {
            emptyDir.mkdir();
        }

        // Llamar al método de eliminación en un directorio vacío
        service.deleteJsonFilesInDirectory(emptyDir.getAbsolutePath(), "any.json");

        // Verificar que el directorio sigue vacío (sin errores)
        assertEquals(0, emptyDir.listFiles().length);

        // Limpiar directorio vacío
        emptyDir.delete();
    }
}
