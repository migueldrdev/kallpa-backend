package dev.migueldr.kallpa_ecommerce.presentation.controller;

import dev.migueldr.kallpa_ecommerce.business.dto.RegisterRequestDto;
import dev.migueldr.kallpa_ecommerce.business.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        String userName = authService.register(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario " + userName + " registrado con éxito"
        ));
    }
}
