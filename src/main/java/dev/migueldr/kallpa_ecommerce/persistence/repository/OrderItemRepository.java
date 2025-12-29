package dev.migueldr.kallpa_ecommerce.persistence.repository;

import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {
}
