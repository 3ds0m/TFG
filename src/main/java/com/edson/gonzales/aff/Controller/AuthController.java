package com.edson.gonzales.aff.Controller;

import com.edson.gonzales.aff.DTO.LoginRequest;
import com.edson.gonzales.aff.DTO.LoginResponse;
import com.edson.gonzales.aff.DTO.LogoutRequest;
import com.edson.gonzales.aff.DTO.RegisterRequest;
import com.edson.gonzales.aff.Entity.User;
import com.edson.gonzales.aff.Repository.UserRepository;
import com.edson.gonzales.aff.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/complete")
    public String complete() {
        return userRepository.findAll().toString();
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        Optional<String> tokenOpt = authService.login(request.getUsernameOrEmail(), request.getPassword());
        System.out.println(request.getUsernameOrEmail() + " " + request.getPassword());
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok(new LoginResponse(tokenOpt.get()));
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }


    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestParam String token) {
        Optional<User> userOpt = authService.validateToken(token);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.getToken());
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }

    @GetMapping("/user/favorites")
    public ResponseEntity<?> getFavorites(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token no proporcionado");
        }
        String token = authHeader.substring(7); // Quita "Bearer "

        Optional<User> userOpt = authService.validateToken(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
        User user = userOpt.get();
        return ResponseEntity.ok(user.getFavorites());
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre de usuario es obligatorio");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El correo electrónico es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!request.getEmail().matches(emailRegex)) {
            return ResponseEntity.badRequest().body("El correo electrónico no tiene un formato válido");
        }
        if (request.getPassword().length() < 4) {
            return ResponseEntity.badRequest().body("La contraseña debe tener al menos 6 caracteres");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(400).body("El nombre de usuario ya está en uso");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("El correo electrónico ya está registrado");
        }
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        authService.saveUser(newUser);
        return ResponseEntity.ok("Usuario creado correctamente");
    }
}
