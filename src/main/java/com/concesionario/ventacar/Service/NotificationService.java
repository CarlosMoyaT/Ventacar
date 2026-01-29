package com.concesionario.ventacar.Service;


import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Model.Vehiculo;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final PdfService pdfService;

    public void enviarConfirmacionCompra(User usuario, Vehiculo vehiculo) throws IOException, DocumentException, MessagingException {

        String fecha = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        File pdfFile = pdfService.createPdf(
                usuario.getEmail(),
                vehiculo.getMarca() + " " + vehiculo.getTipo(),
                fecha,
                vehiculo.getPrecio()
        );

        EmailDTO email = EmailDTO.builder()
                .destinatario(usuario.getEmail())
                .remitente("no-reply@ventacar.com")
                .asunto("Confirmación de compra - Ventacar")
                .cuerpo(construirCuerpoConfirmacion(usuario, vehiculo))
                .html(true)
                .adjuntos(List.of(new EmailAttachmentDTO("Factura.pdf", pdfFile)))
                .build();

        emailService.enviarEmail(email);
    }

    private String construirCuerpoConfirmacion(User usuario, Vehiculo vehiculo) {
        return String.format("""
                <h2>¡Gracias por tu compra, %s!</h2>
                <p>Has adquirido: <strong>%s %s</strong></p>
                <p>Precio: <strong>%s €</strong></p>
                <p>Adjuntamos la factura de tu compra.</p>
                """,
                usuario.getNombre(),
                vehiculo.getMarca(),
                vehiculo.getTipo(),
                vehiculo.getPrecio()
        );
    }


}
