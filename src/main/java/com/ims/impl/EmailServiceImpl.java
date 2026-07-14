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
        headers.set("accept", "application/json");
        headers.set("api-key", apiKey);

        Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                        "name", "IMS Authentication",
                        "email", "atharvaghaware0007@gmail.com"
                ),
                "to", List.of(
                        Map.of(
                                "email", to
                        )
                ),
                "subject", subject,
                "textContent", body
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        try {

        	System.out.println("========== BREVO ==========");
        	System.out.println("API Key Loaded : " + (apiKey != null));
        	System.out.println("API Key Length : " + apiKey.length());
        	System.out.println("API Key Prefix : " + apiKey.substring(0, 8));
        	System.out.println("API Key Suffix : " + apiKey.substring(apiKey.length() - 6));

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            System.out.println("Status : " + response.getStatusCode());
            System.out.println("Body   : " + response.getBody());
            System.out.println("===========================");

        } catch (HttpStatusCodeException ex) {

            System.out.println("========== BREVO ERROR ==========");
            System.out.println("Status  : " + ex.getStatusCode());
            System.out.println("Headers : " + ex.getResponseHeaders());
            System.out.println("Body    : " + ex.getResponseBodyAsString());
            System.out.println("=================================");

            throw new RuntimeException("Brevo API Error", ex);

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new RuntimeException("Unable to send email", ex);

        }
    }
}