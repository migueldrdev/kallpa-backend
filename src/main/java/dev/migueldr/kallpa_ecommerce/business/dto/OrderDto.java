package dev.migueldr.kallpa_ecommerce.business.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Este DTO sirve para MOSTRAR la orden en "Mis Pedidos"
public record OrderDto(
        UUID id,
        LocalDateTime createdAt,
        String status,
        BigDecimal total,
        String contactEmail, // El email que escribió en el checkout
        List<OrderItemResponseDto> items
) {
    public record OrderItemResponseDto(
            UUID productId,
            String productName,
            String productImageUrl,
            Integer quantity,
            BigDecimal price,
            BigDecimal subtotal
    ) {}
}