package com.edson.gonzales.aff.Controller;


import com.edson.gonzales.aff.Service.ApiRequestService;
import com.edson.gonzales.aff.Service.JsonRefineService_Deprecado;
import com.edson.gonzales.aff.Service.JsonToDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiRequestController {
    @Autowired
    private ApiRequestService apiRequestService;
    @Autowired
    private JsonRefineService_Deprecado jsonRefineServiceDeprecado;
    @Autowired
    private JsonToDatabaseService jsonToDatabaseService;

    @GetMapping("/Restaurantes")
    public String processAllAndRefine() {
        try {
            // Rutas necesarias
            String inputFilePath = "src/main/resources/requests.txt";
            String outputDir = "C:/Users/Edson/Desktop/Aff/JSON/";
            String combinedFilePath = outputDir + "combined_results.json";

            // Paso 1: Descargar los JSONs y guardarlos
            apiRequestService.makeRequestsFromTxt(inputFilePath, outputDir);

            // Paso 2: Leer archivos JSON generados y combinarlos
            File dir = new File(outputDir);
            File[] files = dir.listFiles((d, name) -> name.startsWith("response_"));
            if (files == null || files.length == 0) {
                return "No se encontraron archivos JSON para procesar.";
            }
            apiRequestService.combineJsonFilesAndRemoveDuplicates(getFilePaths(files), combinedFilePath);

            // Paso 3: Eliminar los archivos JSON descargados (excluyendo el combinado)
            apiRequestService.deleteJsonFilesInDirectory(outputDir, "combined_results.json");

            // Paso 4(Deprecado): Refinar el archivo combinado (agregar price_range e image)
            //String refineResult =jsonRefineService.processJson();
            //Si se usa este metodo se realizan 434 llamadas a la API

            // Paso 4: El resultado combinado se agrega a base de datos
            jsonToDatabaseService.insertJsonFileToDatabase(combinedFilePath);
            return "Proceso completado: ";

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
