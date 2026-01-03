package dev.migueldr.kallpa_ecommerce.presentation.controller;

import dev.migueldr.kallpa_ecommerce.business.dto.CreateOrderDto;
import dev.migueldr.kallpa_ecommerce.business.dto.OrderDto;
import dev.migueldr.kallpa_ecommerce.business.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    // @Valid activa las validaciones del DTO (@NotBlank, @NotNull)
    public ResponseEntity<Map<String, String>> createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
        UUID orderId = orderService.createOrder(createOrderDto);

        // Devolvemos el ID para que el frontend pueda redirigir a una página de "Gracias /order/uuid"
        return ResponseEntity.ok(Map.of("orderId", orderId.toString(), "message", "Orden creada con éxito"));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDto>> getMyOrders(  ) {
        return ResponseEntity.ok(orderService.getMyOrders());
    }
}