package dev.migueldr.kallpa_ecommerce.business.service;

import dev.migueldr.kallpa_ecommerce.business.dto.CreateOrderDto;
import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.OrderItemEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.OrderRepository;
import dev.migueldr.kallpa_ecommerce.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional // <--- LA CLAVE: Todo ocurre en una sola transacción atómica
    public UUID createOrder(CreateOrderDto input) {

        // 1. Crear la cabecera de la orden
        OrderEntity order = new OrderEntity();
        order.setCustomerName(input.customerName());
        order.setEmail(input.email());
        order.setStatus("PENDING"); // Estado inicial

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
}
