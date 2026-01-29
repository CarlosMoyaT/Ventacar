package com.concesionario.ventacar.Service;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;


    public void sendEmail(EmailDTO emailDTO) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailDTO.getDestinatario());
        helper.setSubject(emailDTO.getAsunto());
        helper.setText(emailDTO.getCuerpo(), emailDTO.isHtml());
        helper.setFrom(emailDTO.getRemitente());

        if (emailDTO.getAdjuntos() != null) {
            for (EmailAttachmentDTO adjunto : emailDTO.getAdjuntos()) {
                helper.addAttachment(adjunto.getNombre(), adjunto.getArchivo());
            }
        }

        mailSender.send(message);
    }

    public void enviarCorreoConfirmacionCompra(String vehiculo, int precio) throws IOException, DocumentException , MessagingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String destinatario = auth.getName();

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File pdfFile = pdfService.createPdf(destinatario, vehiculo, fecha, precio);

        String asunto = "Confirmación de compra";
        String texto = "Gracias por tu compra.\nVehículo: " + vehiculo + "\nPrecio: " + precio + "\nFecha: " + fecha;
        enviarCorreoConAdjunto(destinatario, asunto, texto, pdfFile);

    }

    public void enviarCorreoReserva(String destinatario, String vehiculo, String fechaReserva, int precio)
            throws IOException, DocumentException, MessagingException {

        File pdfFile = pdfService.createPdf(destinatario, vehiculo, fechaReserva, precio);

        String contenidoEmail = "Ha reservado su vehículo: " + vehiculo + "\n" +
                "Fecha de la reserva: " + fechaReserva + "\n" +
                "Precio: " + precio;

        enviarCorreoConAdjunto(destinatario, "Vehículo reservado", contenidoEmail, pdfFile);
    }

    private void enviarCorreoConAdjunto(String destinatario, String asunto, String texto, File adjunto) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(texto);
        helper.setFrom("no-reply@ventacar.com");

        helper.addAttachment(adjunto.getName(), adjunto);

        mailSender.send(message);
    }

    private Date parseFecha(String fechaReserva) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(fechaReserva);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha de reserva no válida: " + fechaReserva);
        }
    }
}