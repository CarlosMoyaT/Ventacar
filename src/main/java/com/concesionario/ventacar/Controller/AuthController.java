package com.concesionario.ventacar.Controller;

import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Service.AuthService;
import com.concesionario.ventacar.Service.EmailService;
import com.concesionario.ventacar.dto.SignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * Controlador para la autenticación de usuarios.
 * Contiene los endpoints necesarios para el registro y el inicio de sesión de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Servicio que gestiona las operaciones de autenticación y registro de usuarios.
     */
    private final AuthService authService;

    @Autowired
    private EmailService emailService;

    /**
     * Constructor que inyecta el servicio de autenticación.
     *
     * @param authService el servicio de autenticación a inyectar.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     * Los parámetros se envían como parámetros de formulario.
     *
     * @param email el correo electrónico del usuario.
     * @param password la contraseña del usuario.
     * @param nombre el nombre del usuario.
     * @param apellidos los apellidos del usuario.
     * @param telefono el número de teléfono del usuario.
     * @param codigoPostal el código postal del usuario.
     * @param fechaNacimiento la fecha de nacimiento del usuario.
     * @param isAdmin si el usuario tiene el rol de administrador (por defecto es "false").
     * @return una respuesta con el resultado de la operación.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDTO request) {
        try {
            authService.registerUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getNombre(),
                    request.getApellidos(),
                    request.getTelefono(),
                    request.getCodigoPostal(),
                    request.getFechaNacimiento(),
                    false
            );
            return ResponseEntity.ok("Usuario registrado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para iniciar sesión en la aplicación.
     * Los datos del usuario se envían como cuerpo de la solicitud en formato JSON.
     *
     * @param user el objeto {@link User} con las credenciales del usuario.
     * @return una respuesta que indica si el inicio de sesión fue exitoso o no.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User authenticatedUser = authService.authenticateUser(user.getEmail(), user.getPassword());
        if (authenticatedUser != null) {
            return ResponseEntity.ok("Login correcto");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login incorrecto");
    }
}