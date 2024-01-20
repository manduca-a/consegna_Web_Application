package org.unical.ingsw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) throws UnsupportedEncodingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setTo(to);
        message.setSubject("Recupero Password");

        String encodedEmail = URLEncoder.encode(to, StandardCharsets.UTF_8.toString());
        String url = "http://localhost:8080/resetPassword?token=" + token + "&email=" + encodedEmail;

        message.setText("Per reimpostare la password, clicca sul link seguente: " + url);
        mailSender.send(message);
    }
}
