package com.concesionario.ventacar.controller;

import com.concesionario.ventacar.Controller.AuthController;
import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Repository.RoleRepository;
import com.concesionario.ventacar.Repository.UserRepository;
import com.concesionario.ventacar.Service.AuthService;
import com.concesionario.ventacar.Service.EmailService;
import com.concesionario.ventacar.dto.SignupRequestDTO;
import com.concesionario.ventacar.dto.LoginRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerTest {

    static class FakeAuthService extends AuthService {
        private boolean shouldFail = false;
        private String errorMessage = "";

        public FakeAuthService(UserRepository userRepo, RoleRepository roleRepo,
                               PasswordEncoder passwordEncoder, EmailService emailService) {
            super(userRepo, roleRepo, passwordEncoder, emailService);
        }

        public FakeAuthService(UserRepository userRepo, RoleRepository roleRepo,
                               PasswordEncoder passwordEncoder, EmailService emailService,
                               String errorMessage) {
            super(userRepo, roleRepo, passwordEncoder, emailService);
            this.shouldFail = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void registerUser(String email, String password, String nombre, String apellidos,
                                 String telefono, String codigoPostal, String fechaNacimiento,
                                 boolean flag) throws Exception {
            if (shouldFail) {
                throw new Exception(errorMessage);
            }
        }

        public FakeAuthService() {
            super(null, null, null, null);
        }

        public FakeAuthService(boolean shouldFail) {
            super(null, null, null, null);
            this.shouldFail = shouldFail;
        }

        @Override
        public User authenticateUser(String email, String password) {
            if (shouldFail) {
                return null;
            }
            return new User();
        }
    }

    @DisplayName("Debería devolver 200 OK cuando el registro de usuario se completa con éxito")
    @Test
    void signUp_should_returnOk_when_registration_succeeds() throws Exception {
        AuthController controller = new AuthController(new FakeAuthService(null,null,null,null));

        SignupRequestDTO request = new SignupRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("123456");
        request.setNombre("Jhon");
        request.setApellidos("Doe");
        request.setTelefono("123456789");
        request.setCodigoPostal("28001");
        request.setFechaNacimiento("1990-01-01");


        ResponseEntity<String> response = controller.signUp(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario registrado", response.getBody());
    }

    @DisplayName("Devolverá error en caso de que el registro de usuario falle")
    @Test
    void signUp_should_return_badRequest_when_registrationFails() {
        AuthController controller = new AuthController(new FakeAuthService(null, null, null, null,"Error de registro"));

        SignupRequestDTO request = new SignupRequestDTO();
        request.setEmail("fail@test.com");
        request.setPassword("123456");
        request.setNombre("Fail");
        request.setApellidos("User");
        request.setTelefono("000000000");
        request.setCodigoPostal("00000");
        request.setFechaNacimiento("2000-01-01");

        ResponseEntity<String> response = controller.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de registro", response.getBody());
    }

    @DisplayName("Devolverá OK si el login se completa con exito")
    @Test
    void login_should_return_login_succeeds() {
        AuthController controller = new AuthController(new FakeAuthService(null, null, null, null));

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("123456");

        ResponseEntity<String> response = controller.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login correcto", response.getBody());


    }

    @Test
    void login_should_return_login_fails() {

    }



}
