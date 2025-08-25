package com.concesionario.ventacar;

import com.concesionario.ventacar.Controller.AuthController;
import com.concesionario.ventacar.Service.AuthService;
import com.concesionario.ventacar.dto.SignupRequestDTO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUpShouldReturnOk() throws Exception {
        SignupRequestDTO request = new SignupRequestDTO();
        //request.setEmail(test@test.com);
        request.setPassword("123456");
        request.setNombre("Jhon");
        request.setApellidos("Dhoe");

        doNothing().when(authService).registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getNombre(),
                request.getApellidos(),
                null, null, null,
                false
        );

        ResponseEntity<String> response = authController.signUp(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuario registrado", response.getBody());
    }

}
