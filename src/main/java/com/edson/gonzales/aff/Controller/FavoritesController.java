package com.edson.gonzales.aff.Controller;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/favorites")
public class FavoritesController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LocationRepository locationRepository;

    @PostMapping("/add/{locationId}")
    public ResponseEntity<?> addFavorite(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String locationId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token no proporcionado");
        }

        String token = authHeader.substring(7);
        var userOpt = authService.validateToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        var user = userOpt.get();

        var locationOpt = locationRepository.findById(locationId);
        if (locationOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Restaurante no encontrado");
        }

        var location = locationOpt.get();
        if (user.getFavorites().contains(location)) {
            return ResponseEntity.status(409).body("Restaurante ya está en favoritos");
        }

        user.getFavorites().add(location);
        authService.saveUser(user);

        return ResponseEntity.ok("Restaurante agregado a favoritos");
    }

    @DeleteMapping("/remove/{locationId}")
    public ResponseEntity<?> removeFavorite(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable String locationId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token no proporcionado");
        }
        String token = authHeader.substring(7);
        var userOpt = authService.validateToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        var user = userOpt.get();

        var locationOpt = locationRepository.findById(locationId);
        if (locationOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Restaurante no encontrado");
        }

        var location = locationOpt.get();
        if (!user.getFavorites().contains(location)) {
            return ResponseEntity.status(409).body("El restaurante no está en la lista de favoritos");
        }

        user.getFavorites().remove(location);
        authService.saveUser(user);

        return ResponseEntity.ok("Restaurante eliminado de favoritos");
    }

}

