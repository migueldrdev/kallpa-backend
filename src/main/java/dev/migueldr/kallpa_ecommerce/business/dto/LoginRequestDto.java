package dev.migueldr.kallpa_ecommerce.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 254, message = "El correo no puede exceder {max} caracteres")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 128, message = "La contraseña debe tener entre {min} y {max} caracteres")
        @Pattern(regexp = "^(?=\\S+$).*$", message = "La contraseña no puede contener espacios en blanco")
        // La contraseña debe tener almenos una letra mayúscula, una minúscula, un número y un carácter especial
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&/])[A-Za-z\\d@$!%*?&/]+$",
                message = "La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial (por ejemplo: @ $ ! % * ? & /)"
        )
        String password
) {
}
