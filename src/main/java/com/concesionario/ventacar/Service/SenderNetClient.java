package com.concesionario.ventacar.Service;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class SenderNetClient {

    @Value("${sender.api.token}")
    private String bearerToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.sender.net/api/v2/email";

    public void sendRegistrationEmail(String to, String subject, String text) {
        Map<String, Object> body = Map.of(
                "from", Map.of("name", "Ventacar", "email", "solrak_27@hotmail.com"),
                "to", List.of(Map.of("email", to)),
                "subject", subject,
                "html", "<pre>" + text + "</pre>"
        );

        sendEmail(body);
    }

    public void sendPurchaseEmail(String to, String subject, String text, File pdfAttachment) throws IOException {
        byte[] pdfBytes = Files.readAllBytes(pdfAttachment.toPath());
        String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

        Map<String, Object> body = Map.of(
                "from", Map.of("name", "Ventacar", "email", "solrak_27@hotmail.com"),
                "to", List.of(Map.of("email", to)),
                "subject", subject,
                "html", "<pre>" + text + "</pre>",
                "attachments", List.of(
                        Map.of(
                                "filename", "Factura.pdf",
                                "type", "application/pdf",
                                "content", pdfBase64
                        )
                )
        );

        sendEmail(body);
    }

    private void sendEmail(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(BASE_URL, request, String.class);
    }

    public String getCampaigns() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        String url = "https://api.sender.net/v2/campaigns";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }
}
