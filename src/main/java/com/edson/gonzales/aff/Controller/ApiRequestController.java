package com.edson.gonzales.aff.Controller;
import com.edson.gonzales.aff.Service.ApiRequestService;
import com.edson.gonzales.aff.Service.JsonRefineService;
import com.edson.gonzales.aff.Service.JsonToDatabaseService;
import com.edson.gonzales.aff.Service.MapService;
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
    @Autowired
    private JsonToDatabaseService jsonToDatabaseService;
    @Autowired
    private JsonRefineService jsonRefineService;
    @Autowired
    private MapService mapService;

    @GetMapping("/Restaurantes")
    public String processAllAndRefine(@RequestParam("apiKey") String apiKey) {
        try {
            // Rutas necesarias
            String inputFilePath = "./Requests/request.txt";
            String outputDir = "./JSON/";
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
            jsonRefineService.setApiKey(apiKey);

            // Paso 4: Refinar el archivo combinado (agregar rating,telefono,cantidad de reviews,rango de precio,tipo de cocina e imagen)
            String refineResult =jsonRefineService.processJson();
            //Si se usa este metodo se realizan hasta (21*Cant_links) llamadas a la API

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
    @GetMapping("/transfer-json")
    public String transferJsonData() {
        // Ruta fija del archivo JSON
        String filePath = "./JSON/updated_results.json";

        try {
            jsonToDatabaseService.insertJsonFileToDatabase(filePath);
            return "✅ Datos transferidos y guardados correctamente en la base de datos";
        } catch (IOException e) {
            return "❌ Error al transferir datos: " + e.getMessage();
        }
    }
    @GetMapping("/ubicacion")
    public String ubicacion() {
        mapService.updateLatLonForIncompleteLocations();
        return "✅ Ubicaciones de los restaurantes actualizada correctamente" ;
    }
}
