package dev.migueldr.kallpa_ecommerce.persistence.repository;

import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
