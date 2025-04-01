package com.edson.gonzales.aff.Service.Prueba;

import com.edson.gonzales.aff.Entity.Prueba.CharacterInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class RickandMortyService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void fetchAndSaveCharacter(int characterId) {
        String url = "https://rickandmortyapi.com/api/character/" + characterId;

        // Hacer la solicitud GET
        CharacterInfo character = restTemplate.getForObject(url, CharacterInfo.class);

        if (character != null) {
            File file= saveJsonToFile(character);
            if (file != null) {
                // Registrar el archivo para eliminarlo al finalizar la aplicación
                registerShutdownHook(file);
            }
        }
    }
    private File saveJsonToFile(CharacterInfo character) {
        LocalDate fecha=LocalDate.now();
        try {
            File file = new File("character_" + character.getName() +"(" +fecha+" )"+ ".json");
            objectMapper.writeValue(file, character);
            System.out.println("JSON guardado en: " + file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void registerShutdownHook(File file) {
        // Crear un hook que se ejecutará al cerrar la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Archivo eliminado: " + file.getName());
                } else {
                    System.out.println("No se pudo eliminar el archivo: " + file.getAbsolutePath());
                }
            } else {
                System.out.println("El archivo no existe cuando el programa termina.");
            }
        }));
    }
}
