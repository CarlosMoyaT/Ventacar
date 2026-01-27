package com.concesionario.ventacar.Controller;

import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Service.AuthService;
import com.concesionario.ventacar.Service.JwtService;
import com.concesionario.ventacar.dto.LoginRequestDTO;
import com.concesionario.ventacar.dto.SignupRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        User authenticatedUser = authService.authenticateUser(request.getEmail(), request.getPassword());
        if (authenticatedUser != null) {
            return ResponseEntity.ok("Login correcto");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login incorrecto");
    }
}