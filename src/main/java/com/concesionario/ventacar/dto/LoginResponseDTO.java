package com.concesionario.ventacar.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String type;
    private String message;

    public LoginResponseDTO(String token, String type) {
        this.token = token;
        this.type = type;
    }
}
