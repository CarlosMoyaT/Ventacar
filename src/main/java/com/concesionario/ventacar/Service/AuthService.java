package com.concesionario.ventacar.Service;

import com.concesionario.ventacar.Model.Role;
import com.concesionario.ventacar.Model.RoleName;
import com.concesionario.ventacar.Model.User;
import com.concesionario.ventacar.Repository.RoleRepository;
import com.concesionario.ventacar.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Servicio de autenticación que maneja la lógica de registro, autenticación y verificación de usuarios.
 * Se encarga de registrar nuevos usuarios, autenticar usuarios existentes y verificar si un correo electrónico ya está registrado.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    /**
     * Constructor de la clase AuthService. Inyecta las dependencias necesarias para la autenticación y gestión de usuarios.
     *
     * @param userRepository el repositorio de usuarios.
     * @param roleRepository el repositorio de roles.
     * @param passwordEncoder el codificador de contraseñas.
     */
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Verifica si el correo electrónico ya está registrado, asigna roles al usuario y guarda el nuevo usuario en la base de datos.
     * El rol predeterminado es "USER", y si el parámetro isAdmin es verdadero, se asigna el rol "ADMIN".
     *
     * @param email el correo electrónico del usuario.
     * @param password la contraseña del usuario.
     * @param nombre el nombre del usuario.
     * @param apellidos los apellidos del usuario.
     * @param telefono el número de teléfono del usuario.
     * @param codigoPostal el código postal del usuario.
     * @param fechaNacimiento la fecha de nacimiento del usuario.
     * @param isAdmin un valor booleano que indica si el usuario debe tener el rol "ADMIN".
     * @throws RuntimeException si el usuario ya existe o si no se pudo asignar ningún rol al usuario.
     */
    public void registerUser(String email, String password, String nombre, String apellidos, String telefono, String codigoPostal, String fechaNacimiento, boolean isAdmin) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("El usuario ya existe");
        }

        Set<Role> roles = new HashSet<>();

        // Asigna el rol "USER" por defecto
        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        userRole.ifPresent(roles::add);

        // Si es admin, asigna el rol "ADMIN"
        if (isAdmin) {
            Optional<Role> adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN);
            adminRole.ifPresent(roles::add);
        }

        // Si no se asignó ningún rol, lanza una excepción
        if (roles.isEmpty()) {
            throw new RuntimeException("No se pudo asignar ningun rol al usuario");
        }

        // Crea el usuario y lo guarda en la base de datos
        User user = new User(email, passwordEncoder.encode(password), nombre, apellidos, telefono, codigoPostal, fechaNacimiento, roles);
        userRepository.save(user);

        emailService.enviarCorreoRegistro(email);
    }



    /**
     * Verifica si un correo electrónico ya está registrado en el sistema.
     *
     * @param email el correo electrónico a verificar.
     * @return {@code true} si el correo electrónico ya está registrado, {@code false} en caso contrario.
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * Autentica a un usuario existente comparando su correo electrónico y contraseña con los valores almacenados en la base de datos.
     *
     * @param email el correo electrónico del usuario.
     * @param password la contraseña proporcionada por el usuario.
     * @return el usuario autenticado si las credenciales son correctas, {@code null} si las credenciales son incorrectas.
     */
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // Si las credenciales son correctas
        }
        return null; // Si las credenciales son incorrectas
    }
}