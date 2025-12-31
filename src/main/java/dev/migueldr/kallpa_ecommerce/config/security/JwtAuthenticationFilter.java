package dev.migueldr.kallpa_ecommerce.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verificar si viene el Token en el formato correcto ("Bearer eyJhbG...")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Si no hay token, pasa (SecurityConfig decidirá si rechaza)
            return;
        }

        // 2. Extraer el token (quitar la palabra "Bearer ")
        jwt = authHeader.substring(7);

        // --- CORRECCIÓN AQUÍ ---
        try {
            // 3. Extraer el email del token
            // Intentamos extraer el email. Si el token es "hola", "123" o expirado,
            // esto lanzará una excepción (MalformedJwtException, ExpiredJwtException, etc.)
            userEmail = jwtService.extractUsername(jwt);

            // 4. Si hay email y el usuario no está autenticado todavía en el contexto...
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // ¡SILENCIO! 🤫
            // Si el token está mal formado o expirado, no hacemos nada.
            // Simplemente NO autenticamos al usuario.
            logger.error("Error validando JWT: " + e.getMessage());
        }

        // Dejamos pasar la petición. Como el SecurityContext está vacío,
        // el SecurityConfig rechazará la petición más adelante sin lanzar errores feos.
        filterChain.doFilter(request, response);
    }
}
