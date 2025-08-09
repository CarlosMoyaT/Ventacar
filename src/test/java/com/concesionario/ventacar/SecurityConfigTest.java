package com.concesionario.ventacar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Ruta pública: debería ser accesible sin autenticación.
     */
    @Test
    void whenAccessPublicRoute_thenOk() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk());
    }

    /**
     * Ruta protegida que requiere autenticación: sin login debe devolver 302 (redirect a login).
     */
    @Test
    void whenAccessProtectedRouteWithoutAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/detalle.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login.html"));
    }

    /**
     * Ruta protegida que requiere rol USER o ADMIN: con usuario rol USER debe acceder correctamente.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenAccessUserRoleRouteWithUser_thenOk() throws Exception {
        mockMvc.perform(get("/detalle.html"))
                .andExpect(status().isOk());
    }

    /**
     * Ruta protegida que requiere rol ADMIN: con usuario rol USER debe dar forbidden (403).
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenAccessAdminRouteWithUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/some-resource"))
                .andExpect(status().isForbidden());
    }

    /**
     * Ruta protegida que requiere rol ADMIN: con usuario rol ADMIN debe acceder.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAccessAdminRouteWithAdmin_thenOk() throws Exception {
        mockMvc.perform(get("/api/admin/some-resource"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba de login exitoso con credenciales correctas.
     * Requiere que el CustomUserDetailsService esté correctamente configurado y cargue usuarios.
     */
    @Test
    void whenPerformLoginWithValidUser_thenRedirectToIndex() throws Exception {
        mockMvc.perform(formLogin("/perform_login")
                        .user("validUser") // Cambia por un usuario válido en tu base de datos de prueba
                        .password("validPassword")) // Cambia por la contraseña correcta
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index.html"));
    }

    /**
     * Prueba de logout redirigiendo a index.
     */
    @Test
    @WithMockUser
    void whenLogout_thenRedirectToIndex() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index.html"));
    }
}

