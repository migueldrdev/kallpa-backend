package dev.migueldr.kallpa_ecommerce.business.dto;

import java.math.BigDecimal;
import java.util.UUID;

// Un 'record' crea getters, constructor, equals y hashcode automáticamente.
public record ProductDto(
        UUID id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl,
        String brand,
        String categoryName // Solo devolvemos el nombre, no el objeto entero
) {}
