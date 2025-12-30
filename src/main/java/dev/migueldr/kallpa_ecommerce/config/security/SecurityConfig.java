package dev.migueldr.kallpa_ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactivamos CSRF porque usaremos JWT (Stateless)
                .authorizeHttpRequests(auth -> auth
                        // Rutas Públicas (Cualquiera puede entrar)
                        .requestMatchers("/api/v1/auth/**").permitAll() // Login y Register
                        .requestMatchers("/api/v1/products/**").permitAll() // Ver productos
                        .requestMatchers("/api/v1/categories/**").permitAll()
                        // El resto requiere autenticación (Lo activaremos en la fase 2)
                        .anyRequest().permitAll() // POR AHORA permitimos todo para no romper tu frontend mientras construimos esto
                );

        return http.build();
    }

    // Bean para encriptar contraseñas (El estándar de la industria)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}