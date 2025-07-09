package com.concesionario.ventacar.Controller;


import com.concesionario.ventacar.Service.SenderNetClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sender")
public class SenderController {

    private final SenderNetClient senderNetClient;

    public SenderController(SenderNetClient senderNetClient) {
        this.senderNetClient = senderNetClient;
    }

    @GetMapping("/campaigns")
    public ResponseEntity<String> listarCampañas() {
        String campañas = senderNetClient.getCampaigns();
        return ResponseEntity.ok(campañas);
    }
}
