package dev.migueldr.kallpa_ecommerce.business.service;

import dev.migueldr.kallpa_ecommerce.business.dto.ProductDto;
import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true) // Optimiza el rendimiento para solo lectura
    public List<ProductDto> findAllProducts() {
        List<ProductEntity> entities = productRepository.findAll();

        // Convertimos de Entity -> DTO usando Streams de Java
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDto findProductBySlug(String slug) {
        ProductEntity entity = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        return mapToDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchProductsByName(String name) {
        List<ProductEntity> entities = productRepository.findByNameContainingIgnoreCase(name);

        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    // Mapeo manual (Más adelante podemos usar MapStruct)
    private ProductDto mapToDto(ProductEntity entity) {
        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImageUrl(),
                entity.getBrand(),
                entity.getCategory() != null ? entity.getCategory().getName() : "Sin Categoría"
        );
    }
}
