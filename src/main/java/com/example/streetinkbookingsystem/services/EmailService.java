package com.example.streetinkbookingsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    //Used regex to verify that the email format is correct
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
