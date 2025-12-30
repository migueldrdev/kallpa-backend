package dev.migueldr.kallpa_ecommerce.business.service;

import dev.migueldr.kallpa_ecommerce.business.dto.RegisterRequestDto;
import dev.migueldr.kallpa_ecommerce.persistence.entity.UserEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    // El método login lo haremos en el siguiente paso cuando configuremos JWT
}