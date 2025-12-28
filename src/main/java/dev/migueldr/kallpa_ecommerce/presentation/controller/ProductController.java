package dev.migueldr.kallpa_ecommerce.presentation.controller;

import dev.migueldr.kallpa_ecommerce.business.dto.ProductDto;
import dev.migueldr.kallpa_ecommerce.business.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products") // Versionado de API (Buena práctica)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /*
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }
    */
    // Paginación: /api/v1/products?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.findAllProducts(pageable));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.findProductBySlug(slug));
    }

    /*
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProductsByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }
    */

    // Búsqueda por nombre paginada: /api/v1/products/search?name=valor&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProductsByName(@RequestParam("name") String name, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProductsByName(name, pageable));
    }
}