package com.proyecto.jpa.controller;

import com.proyecto.jpa.entity.Category;
import com.proyecto.jpa.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de categorías (Capa de Presentación).
 * 
 * <p>Expone endpoints HTTP para realizar operaciones CRUD sobre categorías.
 * Todos los endpoints retornan respuestas en formato JSON.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * CategoryController (Capa de Presentación) ← Esta clase
 *         ↓
 * CategoryService (Capa de Negocio)
 *         ↓
 * CategoryRepository (Capa de Datos)
 * </pre>
 * 
 * <h2>Endpoints Disponibles</h2>
 * <ul>
 *   <li>GET /api/categories - Obtener todas las categorías</li>
 *   <li>GET /api/categories/{id} - Obtener una categoría por ID</li>
 *   <li>POST /api/categories - Crear una nueva categoría</li>
 *   <li>PUT /api/categories/{id} - Actualizar una categoría</li>
 *   <li>DELETE /api/categories/{id} - Eliminar una categoría (en cascada)</li>
 * </ul>
 * 
 * @see Category
 * @see CategoryService
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param categoryService servicio de categorías
     */
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Obtiene todas las categorías.
     * 
     * @return lista de categorías con código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * Obtiene una categoría por su ID.
     * 
     * @param id el ID de la categoría
     * @return la categoría con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva categoría.
     * 
     * @param category la categoría a crear
     * @return la categoría creada con código 201 Created
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        try {
            Category savedCategory = categoryService.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza una categoría existente.
     * 
     * @param id el ID de la categoría a actualizar
     * @param category los nuevos datos de la categoría
     * @return la categoría actualizada con código 200 OK, o 404 Not Found si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody Category category) {
        
        if (!categoryService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        category.setId(id);
        try {
            Category updatedCategory = categoryService.save(category);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina una categoría por su ID.
     * 
     * <p><b>Operación en Cascada:</b> Al eliminar la categoría, todos los productos
     * asociados se eliminarán automáticamente.</p>
     * 
     * @param id el ID de la categoría a eliminar
     * @return código 204 No Content si se eliminó, o 404 Not Found si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cuenta el número total de categorías.
     * 
     * @return el número de categorías con código 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategories() {
        long count = categoryService.count();
        return ResponseEntity.ok(count);
    }
}

