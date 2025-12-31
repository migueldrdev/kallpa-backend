package dev.migueldr.kallpa_ecommerce;

import dev.migueldr.kallpa_ecommerce.persistence.entity.CategoryEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductEntity;
import dev.migueldr.kallpa_ecommerce.persistence.entity.ProductVariantEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.CategoryRepository;
import dev.migueldr.kallpa_ecommerce.persistence.repository.ProductRepository;
import dev.migueldr.kallpa_ecommerce.persistence.repository.ProductVariantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootApplication
public class KallpaEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KallpaEcommerceApplication.class, args);
	}

	// Agrega los nuevos repositorios como argumentos al método
	@Bean
	CommandLineRunner initData(CategoryRepository categoryRepository,
							   ProductRepository productRepository,
							   ProductVariantRepository variantRepository) {
		return args -> {
			if (categoryRepository.count() < 3) {
				System.out.println("--- INICIANDO CARGA ---");

				// 1. Crear Categoría (Como ya tenías)
				CategoryEntity tech = CategoryEntity.builder()
						.name("Tecnología")
						.slug("tecnologia-core")
						.active(true).build();
				CategoryEntity savedCategory = categoryRepository.save(tech);

				// 2. Crear un PRODUCTO (Padre)
				ProductEntity iphone = ProductEntity.builder()
						.name("iPhone 15 Pro")
						.slug("iphone-15-pro")
						.description("El titanio llega al iPhone.")
						.price(new BigDecimal("4500.00")) // Precio base visual
						.category(savedCategory) // Relación
						.brand("Apple")
						.build();

				ProductEntity savedProduct = productRepository.save(iphone);
				System.out.println("Producto guardado: " + savedProduct.getName());

				// 3. Crear VARIANTE 1 (Azul / 256GB) con JSONB
				ProductVariantEntity variant1 = ProductVariantEntity.builder()
						.product(savedProduct)
						.sku("IP15P-BLUE-256")
						.price(new BigDecimal("4800.00"))
						.stockQuantity(10)
						// AQUÍ CREAMOS EL JSON DINÁMICO
						.attributes(Map.of(
								"color", "Titanio Azul",
								"almacenamiento", "256GB",
								"pantalla", "6.1 pulgadas"
						))
						.build();
				variantRepository.save(variant1);

				// 4. Crear VARIANTE 2 (Natural / 512GB) con JSONB
				ProductVariantEntity variant2 = ProductVariantEntity.builder()
						.product(savedProduct)
						.sku("IP15P-NAT-512")
						.price(new BigDecimal("5200.00"))
						.stockQuantity(5)
						.attributes(Map.of(
								"color", "Titanio Natural",
								"almacenamiento", "512GB"
						))
						.build();
				variantRepository.save(variant2);

				System.out.println("Variantes con JSON guardadas exitosamente!");
			}
		};
	}

}
