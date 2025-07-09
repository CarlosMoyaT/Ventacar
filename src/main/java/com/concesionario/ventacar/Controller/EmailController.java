package com.concesionario.ventacar.Controller;

import com.concesionario.ventacar.Service.EmailService;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controlador para gestionar el envío de correos electrónicos.
 * Incluye los endpoints para enviar correos de confirmación y de reserva.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    /**
     * Servicio encargado de gestionar el envío de correos electrónicos.
     */
    @Autowired
    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Endpoint para enviar un correo de confirmación con los detalles de un vehículo.
     * El correo incluye información como el vehículo y el precio.
     *
     * @param vehiculo el nombre del vehículo.
     * @param precio el precio del vehículo.
     * @return una respuesta con el estado de la operación.
     */
    @PostMapping("/confirmacion")
    public ResponseEntity<String> enviarCorreoConfirmacion(
            @RequestParam String vehiculo,
            @RequestParam int precio) {
        try {
            emailService.enviarCorreoConfirmacionCompra(vehiculo, precio);
            return ResponseEntity.ok("Correo de confirmación enviado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el correo de confirmación: " + e.getMessage());
        }
    }

    /**
     * Endpoint para enviar un correo de reserva con los detalles de la reserva de un vehículo.
     * El correo incluye el destinatario, el vehículo, la fecha de reserva y el precio.
     *
     * @param destinatario la dirección de correo electrónico del destinatario.
     * @param vehiculo el nombre del vehículo.
     * @param fechaReserva la fecha de la reserva.
     * @param precio el precio del vehículo.
     * @return una respuesta con el estado de la operación.
     */
    @PostMapping("/reserva")
    public ResponseEntity<String> enviarCorreoReserva(
            @RequestParam String destinatario,
            @RequestParam String vehiculo,
            @RequestParam String fechaReserva,
            @RequestParam int precio) {
        try {
            emailService.enviarCorreoReserva(destinatario, vehiculo, fechaReserva, precio);
            return ResponseEntity.ok("Correo de reserva enviado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el correo de reserva: " + e.getMessage());
        }
    }
}