package com.concesionario.ventacar;

import com.concesionario.ventacar.Controller.AuthController;
import com.concesionario.ventacar.Service.AuthService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @Test
    public void testRegistroUsuarioConExito() {
        Mockito.doThrow(new RuntimeException("Email registrado")).when(authService).registerUser(
                "test@gmail.com",
                "123456",
                "Jhon",
                "Smith",
                "565487785",
                "08001",
                "23-07-1990",
                false
        );
        ResponseEntity<String> response = authController.signUp(
                "test@gmail.com",
                "123456",
                "Jhon",
                "Smith",
                "565487785",
                "08001",
                "23-07-1990",
                false
        );

        verify(authService).registerUser(
                "test@gmail.com",
                "123456",
                "Jhon",
                "Smith",
                "565487785",
                "08001",
                "23-07-1990",
                false
        );

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email registrado", response.getBody());


    }


}
