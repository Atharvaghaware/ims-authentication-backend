package com.ims.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ims.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String body) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("=================================");
            System.out.println("Email sent successfully.");
            System.out.println("To      : " + to);
            System.out.println("Subject : " + subject);
            System.out.println("=================================");

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Failed to send email: " + e.getMessage());

        }
    }
}