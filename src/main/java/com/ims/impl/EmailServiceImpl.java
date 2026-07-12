package com.ims.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ims.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${brevo.api.key}")
	private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendEmail(String to, String subject, String body) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> request = Map.of(
                "sender", Map.of(
                        "name", "IMS Authentication",
                        "email", "atharvaghaware0007@gmail.com"   // Verified sender in Brevo
                ),
                "to", List.of(
                        Map.of("email", to)
                ),
                "subject", subject,
                "textContent", body
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Brevo Response: " + response.getStatusCode());
            System.out.println("Brevo Body: " + response.getBody());

        } catch (HttpStatusCodeException e) {

            System.out.println("Brevo Error Status: " + e.getStatusCode());
            System.out.println("Brevo Error Body: " + e.getResponseBodyAsString());

            throw new RuntimeException("Brevo API Error: " + e.getResponseBodyAsString());

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Unable to send email: " + e.getMessage());

        }
    }
}