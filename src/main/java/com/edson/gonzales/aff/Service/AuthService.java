package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.User;
import com.edson.gonzales.aff.Entity.UserSession;
import com.edson.gonzales.aff.Repository.UserRepository;
import com.edson.gonzales.aff.Repository.UserSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private static final int SESSION_DURATION_HOURS = 24;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<String> login(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
            if (userOpt.isEmpty()) {
                System.out.println("[LOGIN] Usuario no encontrado (ni por username ni email): " + usernameOrEmail);
                return Optional.empty();
            }
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("[LOGIN] Contraseña incorrecta para usuario: " + usernameOrEmail);
            return Optional.empty();
        }
        String token = UUID.randomUUID().toString();
        UserSession session = UserSession.builder()
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(SESSION_DURATION_HOURS))
                .build();

        userSessionRepository.save(session);
        return Optional.of(token);
    }


    // Validar token: devuelve usuario si el token es válido y no expiró
    public Optional<User> validateToken(String token) {
        Optional<UserSession> sessionOpt = userSessionRepository.findByToken(token);
        if (sessionOpt.isEmpty()) {
            return Optional.empty();
        }
        UserSession session = sessionOpt.get();
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("[VALIDATE TOKEN] Token expirado, eliminando sesión: " + token);
            userSessionRepository.delete(session);
            return Optional.empty();
        }
        System.out.println("[VALIDATE TOKEN] Token válido para usuario: " + session.getUser().getUsername());
        return Optional.of(session.getUser());
    }

    // Logout: elimina la sesión con el token dado
    @Transactional
    public void logout(String token) {
        userSessionRepository.deleteByToken(token);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
