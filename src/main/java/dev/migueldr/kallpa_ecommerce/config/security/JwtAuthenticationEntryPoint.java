package dev.migueldr.kallpa_ecommerce.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Usamos Jackson para convertir el objeto de error a JSON manualmente
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // 1. Definimos el status 401 (Unauthorized) en lugar del 403 confuso
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 2. Construimos el JSON de error estándar (ProblemDetail)
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "No autenticado");
        problemDetail.setTitle("Acceso Denegado");
        problemDetail.setProperty("description", "Debes incluir un Token JWT válido en el header Authorization");
        problemDetail.setProperty("error_category", "Security");

        // 3. Escribimos el JSON en la respuesta
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}