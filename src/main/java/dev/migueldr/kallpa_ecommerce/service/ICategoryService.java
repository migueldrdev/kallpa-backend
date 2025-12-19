package dev.migueldr.kallpa_ecommerce.service;

import dev.migueldr.kallpa_ecommerce.persistence.entity.CategoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICategoryService {
    // Obtener la lista de todos los clientes
    List<CategoryEntity> listAllCategory();
    // Obtener un category por su ID
    // Devuelve un Optional que puede estar vacío si no se encuentra el category
    Optional<CategoryEntity> getCategoryById(UUID id);
    /*
        Sirve para crear o actualizar un category
        Si el category tiene un id existente, se actualiza; si no, se crea uno nuevo
    */
    CategoryEntity saveCategory(CategoryEntity category);
    // Eliminar un category por su ID
    boolean deleteCategory(UUID id);
}
