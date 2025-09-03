package com.concesionario.ventacar.Controller;

import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Service.AuthService;
import com.concesionario.ventacar.Service.EmailService;
import com.concesionario.ventacar.dto.LoginRequestDTO;
import com.concesionario.ventacar.dto.SignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * Controlador REST encargado de la autenticación de usuarios en la aplicación.
 * <p>
 * Proporciona endpoints para el registro de nuevos usuarios y el inicio de sesión.
 * </p>
 * <p>
 * URL base: <code>/api/auth</code>
 * </p>
 *
 * @since 1.0
 * @version 1.0
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


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * <p>
     * Recibe un {@link SignupRequestDTO} con los datos necesarios para el registro.
     * </p>
     *
     * @param request DTO que contiene los datos de registro del usuario.
     * @return {@link ResponseEntity} con mensaje de éxito o error.
     * @throws Exception si ocurre algún error durante el registro.
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
     * Endpoint para autenticar un usuario existente.
     * <p>
     * Recibe un objeto {@link User} con el correo y la contraseña.
     * Si la autenticación es correcta, devuelve un mensaje de éxito.
     * Si falla, devuelve un mensaje de error con el estado HTTP 401.
     * </p>
     *
     * @param user Usuario que intenta iniciar sesión.
     * @return {@link ResponseEntity} con mensaje de éxito o fallo de autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        User authenticatedUser = authService.authenticateUser(request.getEmail(), request.getPassword());
        if (authenticatedUser != null) {
            return ResponseEntity.ok("Login correcto");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login incorrecto");
    }
}