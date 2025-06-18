package com.concesionario.ventacar.dto;

import com.concesionario.ventacar.Model.Role;

import java.util.Set;

public class UserDTO {

    private Long id;
    private String email;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String codigoPostal;
    private String fechaNacimiento;
    private Set<Role> roles;
}
