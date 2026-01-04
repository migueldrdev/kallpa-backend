package dev.migueldr.kallpa_ecommerce.business.service;

import dev.migueldr.kallpa_ecommerce.business.dto.CreateOrderDto;
import dev.migueldr.kallpa_ecommerce.business.dto.OrderDto;
import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderItemEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.UserEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.OrderRepository;
import dev.migueldr.kallpa_ecommerce.persistence.repository.ProductRepository;
import dev.migueldr.kallpa_ecommerce.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional // <--- LA CLAVE: Todo ocurre en una sola transacción atómica
    public UUID createOrder(CreateOrderDto input) {

        // 1. Crear la cabecera de la orden
        OrderEntity order = new OrderEntity();
        order.setCustomerName(input.customerName());
        order.setEmail(input.email());
        order.setStatus("PENDING"); // Estado inicial

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!"anonymousUser".equals(currentUserEmail)) {
            userRepository.findByEmail(currentUserEmail)
                    .ifPresent(order::setUser); // ¡Aquí ocurre la magia! FK user_id
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 2. Procesar cada ítem
        for (CreateOrderDto.OrderItemDto itemDto : input.items()) {

            // A. Buscar producto en BD (Para obtener precio real y stock actual)
            ProductEntity product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

            // B. Validar Stock
            if (product.getStock() < itemDto.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + product.getName());
            }

            // C. Descontar Stock (Actualizamos el inventario)
            product.setStock(product.getStock() - itemDto.quantity());
            productRepository.save(product); // Guardamos el nuevo stock

            // D. Crear el detalle de la orden
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setPrice(product.getPrice()); // <--- SNAPSHOT DEL PRECIO REAL

            // E. Calcular subtotal y sumar al total
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            totalAmount = totalAmount.add(itemTotal);

            // F. Vincular (Helper method que creaste en la entidad)
            order.addItem(orderItem);
        }

        // 3. Asignar total calculado por nosotros (Seguro)
        order.setTotal(totalAmount);

        // 4. Guardar todo (Gracias al CascadeType.ALL, guarda orden + items)
        OrderEntity savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    // Método para obtener "Mis Pedidos" mapeado al DTO completo
    public List<OrderDto> getMyOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en contexto"));

        // Buscamos por ID de usuario (Seguro)
        List<OrderEntity> entities = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        // Convertimos Entidad -> DTO
        return entities.stream().map(this::mapToDto).toList();
    }

    // Método auxiliar para mapear OrderEntity a OrderDto
    private OrderDto mapToDto(OrderEntity entity) {
        List<OrderDto.OrderItemResponseDto> items = entity.getItems().stream()
                .map(item -> new OrderDto.OrderItemResponseDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                )).toList();

        return new OrderDto(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getStatus(),
                entity.getTotal(),
                entity.getEmail(),
                items
        );
    }
}
