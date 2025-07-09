package com.concesionario.ventacar.Service;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.net.http.HttpHeaders;
import java.util.List;

@Service
public class SenderNetClient {

    @Value("${sender.api.token}")
    private String bearerToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.sender.net/v2";

    public String getCampaigns() {
        String url = BASE_URL + "/campaigns/";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setAccept(List.of(PageAttributes.MediaType.APPLICACTION_JSON));
        HttpEntity<Void> req = new HttpEntity<>(headers);

        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, req, String.class);
        return resp.getBody();
    }
}
