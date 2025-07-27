package com.concesionario.ventacar.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import com.concesionario.ventacar.Service.CustomUserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Configura la seguridad de Spring Security, incluyendo autorización,
 * autenticación, codificación de contraseñas y rutas públicas/protegidas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Servicio personalizado para la carga de detalles de usuario.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Configura la cadena de filtros de seguridad para manejar
     * la autorización y autenticación HTTP.
     *
     * @param http el objeto {@link HttpSecurity} utilizado para construir la configuración.
     * @return el {@link SecurityFilterChain} configurado.
     * @throws Exception si ocurre algún error al construir la cadena de filtros.
     */
    @Bean
    @Order
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index.html", "/login.html", "/resultados.html",
                                "/css/**", "/js/**", "/img/**", "/favicon.ico",
                                "/nosotros.html", "/contacto.html"
                        ).permitAll()
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/vehiculos/buscar",
                                "/api/vehiculos/**"
                        ).permitAll()
                        .requestMatchers("/detalle.html").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/pdf/generate", "/api/email/confirmacion", "/api/email/reserva").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/index.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:63342"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }



    /**
     * Crea un bean de codificador de contraseñas usando BCrypt.
     *
     * @return una instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el proveedor de autenticación que usa un servicio de detalles de usuario
     * y un codificador de contraseñas.
     *
     * @return el {@link DaoAuthenticationProvider} configurado.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expone un bean de {@link AuthenticationManager} a partir de la configuración proporcionada.
     *
     * @param config la configuración de autenticación.
     * @return el {@link AuthenticationManager} configurado.
     * @throws Exception si ocurre un error al obtener el administrador de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}