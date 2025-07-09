package com.concesionario.ventacar.Controller;


import com.concesionario.ventacar.Service.SenderNetClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sender")
public class SenderController {

    private final SenderNetClient senderNetClient;

    public SenderController(SenderNetClient senderNetClient) {
        this.senderNetClient = senderNetClient;
    }

    @GetMapping("/campaigns")
    public ResponseEntity<String> listCampaigns() {
        String campaigns = senderNetClient.getCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("/send-registration")
    public ResponseEntity<String> enviarCorreoRegistro(@RequestParam String email) {
        senderNetClient.sendRegistrationEmail(email, "Registro en Ventacar",
                "Gracias por registrarte en Ventacar.");
        return ResponseEntity.ok("Correo de registro enviado.");
    }


}
