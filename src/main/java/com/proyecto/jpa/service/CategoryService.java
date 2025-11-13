package com.proyecto.jpa.service;

import com.proyecto.jpa.entity.Category;
import com.proyecto.jpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de categorías (Capa de Negocio).
 * 
 * <p>Implementa la lógica de negocio relacionada con las categorías de productos,
 * coordinando las operaciones con el repositorio y aplicando reglas de negocio.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * CategoryController (Capa de Presentación)
 *         ↓
 * CategoryService (Capa de Negocio) ← Esta clase
 *         ↓
 * CategoryRepository (Capa de Datos)
 * </pre>
 * 
 * <h2>Operaciones en Cascada</h2>
 * <p>Al eliminar una categoría, todos sus productos asociados se eliminan
 * automáticamente gracias a la configuración {@code cascade = CascadeType.ALL}
 * en la entidad Category.</p>
 * 
 * @see Category
 * @see CategoryRepository
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param categoryRepository repositorio de categorías
     */
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Obtiene todas las categorías del sistema.
     * 
     * @return lista de todas las categorías
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Busca una categoría por su ID.
     * 
     * @param id el ID de la categoría
     * @return un Optional con la categoría si existe
     */
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Busca una categoría por su nombre.
     * 
     * @param name el nombre de la categoría
     * @return un Optional con la categoría si existe
     */
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Verifica si existe una categoría con el nombre dado.
     * 
     * @param name el nombre a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * Crea o actualiza una categoría.
     * 
     * <p>Si la categoría no tiene ID, se crea una nueva. Si tiene ID, se actualiza.</p>
     * 
     * @param category la categoría a guardar
     * @return la categoría guardada con su ID asignado
     * @throws IllegalArgumentException si la categoría es null o el nombre está vacío
     */
    @Transactional
    public Category save(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("La categoría no puede ser null");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        
        // Validar nombre único (solo para nuevas categorías)
        if (category.getId() == null && existsByName(category.getName())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + category.getName());
        }
        
        return categoryRepository.save(category);
    }

    /**
     * Elimina una categoría por su ID.
     * 
     * <p><b>Operación en Cascada:</b> Al eliminar la categoría, todos los productos
     * asociados se eliminarán automáticamente debido a la configuración de cascada.</p>
     * 
     * @param id el ID de la categoría a eliminar
     * @throws IllegalArgumentException si no existe una categoría con ese ID
     */
    @Transactional
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una categoría con ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Cuenta el número total de categorías.
     * 
     * @return el número total de categorías
     */
    public long count() {
        return categoryRepository.count();
    }
}

