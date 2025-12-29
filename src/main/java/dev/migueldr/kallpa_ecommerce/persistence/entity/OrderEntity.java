package dev.migueldr.kallpa_ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    // Datos del cliente (Simple por ahora, sin tabla de usuarios)
    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String email;

    // Estado: PENDING, PAID, SHIPPED, CANCELLED
    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Relación Uno a Muchos: Una orden tiene muchos ítems
    // Cascade.ALL: Si guardo la Orden, se guardan los Ítems automáticamente
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    // Helper para agregar ítems y mantener la coherencia bidireccional
    public void addItem(OrderItemEntity item) {
        items.add(item);
        item.setOrder(this);
    }
}