package dev.migueldr.kallpa_ecommerce.persistence.repository;

import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findBySlug(String slug);
    // Spring traducirá esto a:
    // SELECT * FROM products WHERE UPPER(name) LIKE UPPER('%termino%')
    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    Page<ProductEntity> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
