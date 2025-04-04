package com.edson.gonzales.aff.Controller;

import com.edson.gonzales.aff.Service.ApiRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiRequestController {

    @Autowired
    private ApiRequestService apiRequestService;

    @GetMapping("/process-all")
    public String processAll() {
        try {
            // Ruta del archivo txt de solicitudes y el directorio de salida
            String inputFilePath = "src/main/resources/requests.txt";
            String outputDir = "C:/Users/Edson/Desktop/Aff/JSON/";

            // Paso 1: Hacer las solicitudes y guardar los archivos JSON
            apiRequestService.makeRequestsFromTxt(inputFilePath, outputDir);

            // Paso 2: Leer los archivos JSON generados
            File dir = new File(outputDir);
            File[] files = dir.listFiles((d, name) -> name.startsWith("response_"));
            if (files == null || files.length == 0) {
                return "No se encontraron archivos JSON para procesar.";
            }

            // Paso 3: Combinar los archivos JSON y eliminar duplicados
            String combinedFilePath = outputDir + "combined_results.json";
            apiRequestService.combineJsonFilesAndRemoveDuplicates(getFilePaths(files), combinedFilePath);

            // Paso 4: Eliminar los archivos JSON descargados (excluyendo el archivo combinado)
            apiRequestService.deleteJsonFilesInDirectory(outputDir, "combined_results.json");

            return "Proceso completado: solicitudes procesadas, combinadas, duplicados eliminados y archivos descargados eliminados, excepto el combinado.";
        } catch (IOException e) {
            return "Error durante el proceso: " + e.getMessage();
        }
    }

    // Método auxiliar para obtener las rutas de los archivos
    private List<String> getFilePaths(File[] files) {
        List<String> filePaths = new ArrayList<>();
        for (File file : files) {
            filePaths.add(file.getAbsolutePath());
        }
        return filePaths;
    }
}
