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
 * Configuración de seguridad para la aplicación del concesionario.
 *
 * <p>Esta clase define las reglas de autenticación, autorización,
 * configuración de CORS, cifrado de contraseñas y manejo de autenticación
 * personalizada para la aplicación.</p>
 *
 * <p>Utiliza Spring Security para proteger las rutas, permitiendo
 * el acceso público a ciertos recursos y restringiendo otros según el rol
 * del usuario.</p>
 *
 * @author Carlos
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Servicio personalizado para la carga de detalles de usuario,
     * utilizado en la autenticación.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * <p>Define:</p>
     * <ul>
     *   <li>Rutas públicas y protegidas según rol.</li>
     *   <li>Configuración de login y logout.</li>
     *   <li>Desactivación de CSRF y activación de CORS.</li>
     * </ul>
     *
     * @param http objeto {@link HttpSecurity} para configurar las reglas.
     * @return instancia de {@link SecurityFilterChain} con la configuración aplicada.
     * @throws Exception si ocurre un error durante la configuración.
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

    /**
     * Configuración de CORS para permitir solicitudes desde el frontend.
     *
     * @return un {@link CorsConfigurationSource} con orígenes, métodos y cabeceras permitidas.
     */
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
     * Define el codificador de contraseñas a utilizar.
     *
     * @return un {@link PasswordEncoder} basado en {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación que utiliza {@link CustomUserDetailsService}
     * y {@link BCryptPasswordEncoder}.
     *
     * @return un {@link DaoAuthenticationProvider} configurado.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Gestiona la autenticación en la aplicación.
     *
     * @param config configuración de autenticación de Spring.
     * @return un {@link AuthenticationManager} para procesar autenticaciones.
     * @throws Exception si ocurre un error al obtener el gestor de autenticación.
     */1
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}