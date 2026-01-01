package dev.migueldr.kallpa_ecommerce.persistence.repository;

import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    // Buscar por email ordenado por fecha descendente
    List<OrderEntity> findByEmailOrderByCreatedAtDesc(String email);

    // Versión paginada (Mejor práctica)
    Page<OrderEntity> findByEmailOrderByCreatedAtDesc(String email, Pageable pageable);
}
