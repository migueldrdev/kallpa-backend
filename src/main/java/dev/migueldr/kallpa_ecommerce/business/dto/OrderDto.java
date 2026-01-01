package dev.migueldr.kallpa_ecommerce.business.dto;

public record OrderDto(
    String customerName,
    String email,
    String status,
    java.math.BigDecimal totalAmount,
    java.time.LocalDateTime createdAt,
    java.util.List<OrderItemDto> items
) {
    public record OrderItemDto(
        java.util.UUID productId,
        String productName,
        Integer quantity,
        java.math.BigDecimal price
    ) {}
}
