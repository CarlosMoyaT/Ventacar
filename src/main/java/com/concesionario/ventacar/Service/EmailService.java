package com.concesionario.ventacar.Service;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

/**
 * Servicio para el envío de correos electrónicos con archivos PDF adjuntos,
 * como confirmaciones de compra o reservas de vehículos.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private PdfService pdfService;

    public void enviarCorreoRegistro(String destinatario) {
        String subject = "Confirmación de registro";
        String text = "Gracias por registrarte en Ventacar.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("no-reply@ventacar.com");

        mailSender.send(message);
    }

    /**
     * Envía un correo de confirmación de compra al usuario autenticado,
     * incluyendo un archivo PDF adjunto con los detalles.
     *
     * @param vehiculo nombre o descripción del vehículo comprado.
     * @param precio   precio del vehículo.
     * @throws MessagingException si ocurre un error al enviar el correo.
     * @throws IOException        si ocurre un error al generar el PDF.
     * @throws DocumentException  si ocurre un error en el documento PDF.
     */
    public void enviarCorreoConfirmacionCompra(String vehiculo, int precio) throws IOException, DocumentException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String destinatario = auth.getName();

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File pdfFile = pdfService.createPdf(destinatario, vehiculo, fecha, precio);

        String asunto = "Confirmación de compra";
        String texto = "Gracias por tu compra.\nVehículo: " + vehiculo + "\nPrecio: " + precio + "\nFecha: " + fecha;
        enviarCorreoConAdjunto(destinatario, asunto, texto, pdfFile);

    }

    /**
     * Envía un correo de confirmación de reserva con un archivo PDF adjunto.
     *
     * @param destinatario   dirección de correo del cliente.
     * @param vehiculo       nombre o modelo del vehículo reservado.
     * @param fechaReserva   fecha en formato 'yyyy-MM-dd'.
     * @param precio         precio del vehículo reservado.
     * @throws MessagingException si ocurre un error al enviar el correo.
     * @throws IOException        si ocurre un error al generar el PDF.
     * @throws DocumentException  si ocurre un error en el documento PDF.
     */
    public void enviarCorreoReserva(String destinatario, String vehiculo, String fechaReserva, int precio)
            throws IOException, DocumentException {

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

    /**
     * Convierte una fecha en formato texto ('yyyy-MM-dd') a un objeto {@link Date}.
     *
     * @param fechaReserva la fecha como cadena.
     * @return un objeto {@link Date} representando la fecha.
     * @throws IllegalArgumentException si el formato es inválido.
     */
    private Date parseFecha(String fechaReserva) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(fechaReserva);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha de reserva no válida: " + fechaReserva);
        }
    }
}