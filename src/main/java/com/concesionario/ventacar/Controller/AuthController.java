package com.concesionario.ventacar.Controller;

import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Service.AuthService;
import com.concesionario.ventacar.Service.JwtService;
import com.concesionario.ventacar.dto.LoginRequestDTO;
import com.concesionario.ventacar.dto.LoginResponseDTO;
import com.concesionario.ventacar.dto.SignupRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    //Crear el LoginResponseDTO
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);


            return ResponseEntity.ok(new LoginResponseDTO(jwt, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(null, "Incorrect login"));
        }
    }
}

