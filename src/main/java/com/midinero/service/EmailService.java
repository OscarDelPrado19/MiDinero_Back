package com.midinero.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailRecuperacion(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recuperación de Contraseña - MiDinero");
            message.setText("Hola,\n\n" +
                "Has solicitado recuperar tu contraseña. Usa el siguiente token para resetear tu contraseña:\n\n" +
                "Token: " + token + "\n\n" +
                "Este token expirará en 1 hora.\n\n" +
                "Si no solicitaste este cambio, ignora este email.\n\n" +
                "Saludos,\n" +
                "Equipo MiDinero");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }
}
