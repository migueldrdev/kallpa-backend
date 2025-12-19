package dev.migueldr.kallpa_ecommerce.service;

import dev.migueldr.kallpa_ecommerce.persistence.entity.CategoryEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    /**
     * @return List<CategoryEntity>
     */
    @Override
    @Cacheable(value = "clientsCache", key = "'allClients'")
    public List<CategoryEntity> listAllCategory() {
        return categoryRepository.findAll();
    }

    /**
     * @param
     * @return
     */
    @Override
    public Optional<CategoryEntity> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    /**
     * @param category 
     * @return
     */
    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public boolean deleteCategory(UUID id) {
        // Eliminar el cliente por ID
        if (!categoryRepository.existsById(id)){
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
