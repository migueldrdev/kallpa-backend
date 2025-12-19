package dev.migueldr.kallpa_ecommerce.persistence.repository;

import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
