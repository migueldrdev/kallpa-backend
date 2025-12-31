package dev.migueldr.kallpa_ecommerce.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permitir tu Frontend (Angular)
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // 2. Permitir Métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 3. Permitir Headers (¡AQUÍ ESTÁ EL PROBLEMA!)
        // Debemos permitir explícitamente 'Authorization' para que pase el Token
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));

        // 4. Permitir credenciales (cookies, etc) si hiciera falta
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica a toda la API

        return new CorsFilter(source);
    }
}
