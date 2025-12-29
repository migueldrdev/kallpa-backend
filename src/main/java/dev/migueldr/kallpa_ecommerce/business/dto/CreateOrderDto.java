package dev.migueldr.kallpa_ecommerce.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record CreateOrderDto(
        @NotBlank(message = "El nombre es obligatorio")
        String customerName,
        @NotBlank @Email(message = "El email debe ser válido")
        String email,
        @NotEmpty(message = "La orden debe tener productos")
        List<OrderItemDto> items
) {
    public record OrderItemDto(
            @NotNull UUID productId,
            @NotNull Integer quantity
    ) {}
}
