package dev.migueldr.kallpa_ecommerce.config.security;

import dev.migueldr.kallpa_ecommerce.persistence.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // CLAVE SECRETA: En producción esto va en variables de entorno.
    // Debe ser larga y aleatoria (256 bits).
    private static final String SECRET_KEY = "kallpa_ecommerce_secret_key_super_segura_para_firmar_tokens_jwt_2025";

    public String generateToken(UserEntity user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId().toString());
        extraClaims.put("role", user.getRole());
        extraClaims.put("name", user.getFullName());

        return Jwts.builder().setClaims(extraClaims).setSubject(user.getEmail()) // El "usuario" principal del token
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas de validez
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    // --- Métodos para validar el token (Los usaremos en la Fase 3) ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        // Si usas una clave texto simple, asegúrate que sea larga, si no JJWT fallará
        // Aquí simulamos base64, o usamos Keys.hmacShaKeyFor si es texto plano bytes
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}