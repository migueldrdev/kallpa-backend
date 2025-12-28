package dev.migueldr.kallpa_ecommerce.presentation.controller;

import dev.migueldr.kallpa_ecommerce.business.dto.ProductDto;
import dev.migueldr.kallpa_ecommerce.business.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products") // Versionado de API (Buena práctica)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.findProductBySlug(slug));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProductsByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }
}