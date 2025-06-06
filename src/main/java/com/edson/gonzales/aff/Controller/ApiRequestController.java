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

    @GetMapping("/project-status")
    public String projectStatus() {
        StringBuilder sb = new StringBuilder();
        String baseDir = System.getProperty("user.dir");

        sb.append("=== Estructura completa del proyecto desde: ").append(baseDir).append(" ===\n\n");

        File base = new File(baseDir);
        listDirectoryRecursively(base, sb, 0);

        sb.append("\n=== Verificación detallada de carpetas clave ===\n\n");

        String[] importantDirs = {"Requests", "JSON"};
        for (String dirName : importantDirs) {
            sb.append("Directorio '").append(dirName).append("':\n");
            File dir = new File(baseDir, dirName);

            if (!dir.exists()) {
                sb.append("  -> No existe el directorio\n\n");
                continue;
            }
            if (!dir.isDirectory()) {
                sb.append("  -> No es un directorio válido\n\n");
                continue;
            }
            if (!dir.canRead()) {
                sb.append("  -> No se tienen permisos para leer el directorio\n\n");
                continue;
            }

            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                sb.append("  -> El directorio está vacío\n\n");
                continue;
            }

            for (File f : files) {
                sb.append("  - ").append(f.getName());
                if (f.isDirectory()) sb.append(" [DIRECTORIO]");
                else sb.append(" [ARCHIVO]");
                sb.append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void listDirectoryRecursively(File dir, StringBuilder sb, int indent) {
        if (!dir.exists()) {
            sb.append(getIndent(indent)).append("-> No existe: ").append(dir.getName()).append("\n");
            return;
        }
        if (!dir.canRead()) {
            sb.append(getIndent(indent)).append("-> Sin permiso lectura: ").append(dir.getName()).append("\n");
            return;
        }
        if (dir.isDirectory()) {
            sb.append(getIndent(indent)).append("[DIR] ").append(dir.getName()).append("\n");
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                sb.append(getIndent(indent + 1)).append("(vacío)\n");
                return;
            }
            for (File f : files) {
                listDirectoryRecursively(f, sb, indent + 1);
            }
        } else if (dir.isFile()) {
            sb.append(getIndent(indent)).append("[FILE] ").append(dir.getName()).append("\n");
        } else {
            sb.append(getIndent(indent)).append("[OTRO] ").append(dir.getName()).append("\n");
        }
    }

    private String getIndent(int indent) {
        return "  ".repeat(Math.max(0, indent));
    }
}
