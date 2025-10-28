package com.proyecto.jpa.repository;

import com.proyecto.jpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Category.
 * 
 * JpaRepository proporciona operaciones CRUD automáticas:
 * - save(S entity): guarda una entidad
 * - findById(ID id): busca por ID
 * - findAll(): obtiene todas las entidades
 * - deleteById(ID id): elimina por ID
 * - count(): cuenta el número de entidades
 * 
 * También se pueden definir consultas personalizadas usando
 * la convención de nombres de métodos de Spring Data JPA.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Busca una categoría por su nombre.
     * Spring Data JPA genera automáticamente la consulta:
     * SELECT * FROM categories WHERE name = ?
     * 
     * @param name nombre de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Category> findByName(String name);

    /**
     * Verifica si existe una categoría con el nombre dado.
     * 
     * @param name nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}

