package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.User;
import com.edson.gonzales.aff.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean generateResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            System.out.println("[RESET] Email no encontrado: " + email);
            return false;
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        sendResetEmail(email, token);  // Aquí se llama al envío de correo

        return true;
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findByResetToken(token);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return true;
    }

    private void sendResetEmail(String toEmail, String token) {
        String resetUrl = "http://127.0.0.1:5500/reset-password.html?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Restablece tu contraseña - MadridLocal");
        message.setText("Hola,\n\nHaz clic en el siguiente enlace para cambiar tu contraseña:\n" + resetUrl + "\n\nSi no solicitaste este cambio, ignora este correo. \n\nEste link expirara en 30 minutos");
        mailSender.send(message);
    }
}
