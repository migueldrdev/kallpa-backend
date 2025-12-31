package dev.migueldr.kallpa_ecommerce.business.service;

import dev.migueldr.kallpa_ecommerce.business.dto.LoginRequestDto;
import dev.migueldr.kallpa_ecommerce.business.dto.RegisterRequestDto;
import dev.migueldr.kallpa_ecommerce.config.security.JwtService;
import dev.migueldr.kallpa_ecommerce.persistence.entity.UserEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String register(RegisterRequestDto request) {
        // 1. Validar que el email no exista
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        // 2. Crear usuario
        UserEntity user = new UserEntity();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setRole("ROLE_USER"); // Por defecto todos son usuarios normales

        // 3. Encriptar contraseña (¡CRÍTICO!)
        user.setPassword(passwordEncoder.encode(request.password()));

        UserEntity saveRegister = userRepository.save(user);

        return request.fullName();
    }

    public String login(LoginRequestDto request) {
        // 1. Spring Security intenta autenticar (Verifica email y pass match hash)
        // Si falla, lanza AuthenticationException automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Si llegamos aquí, las credenciales son correctas. Buscamos al usuario.
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 3. Generamos su brazalete (Token)
        return jwtService.generateToken(user);
    }
}