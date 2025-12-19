package dev.migueldr.kallpa_ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Patrón Builder para crear objetos fácil
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Nuevo estándar en Spring Boot 3 / Hibernate 6
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "parent_id")
    private UUID parentId;

    /* NOTA AVANZADA: Podríamos mapear 'CategoryEntity parent' con @ManyToOne,
       pero para empezar y evitar "LazyInitializationException" (muy común en novatos),
       mapear solo el ID es una estrategia válida llamada "Id Reference Pattern".
       Más adelante te enseñaré a relacionar objetos completos si lo necesitamos.
    */

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist // Se ejecuta antes de guardar
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.active == null) this.active = true;
    }
}