package com.proyecto.jpa.repository;

import com.proyecto.jpa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para la entidad Product.
 * Demuestra diferentes formas de crear consultas personalizadas.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca productos por categoría (usando nombre de método).
     * 
     * @param categoryId ID de la categoría
     * @return lista de productos de esa categoría
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Busca productos cuyo nombre contenga el texto dado (case-insensitive).
     * 
     * @param name texto a buscar en el nombre
     * @return lista de productos que coinciden
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Busca productos en un rango de precios.
     * 
     * @param minPrice precio mínimo
     * @param maxPrice precio máximo
     * @return lista de productos en ese rango
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Busca productos con stock disponible.
     * 
     * @return lista de productos con stock > 0
     */
    List<Product> findByStockGreaterThan(Integer stock);

    /**
     * Consulta personalizada usando JPQL (Java Persistence Query Language).
     * Busca productos por categoría ordenados por precio.
     * 
     * @param categoryId ID de la categoría
     * @return lista de productos ordenados por precio ascendente
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY p.price ASC")
    List<Product> findProductsByCategoryOrderByPrice(@Param("categoryId") Long categoryId);

    /**
     * Consulta nativa SQL.
     * Busca los productos más vendidos (top N).
     * 
     * @param limit número de productos a retornar
     * @return lista de productos más vendidos
     */
    @Query(value = "SELECT p.* FROM products p " +
                   "LEFT JOIN order_items oi ON p.product_id = oi.product_id " +
                   "GROUP BY p.product_id " +
                   "ORDER BY COUNT(oi.order_item_id) DESC " +
                   "LIMIT :limit", 
           nativeQuery = true)
    List<Product> findTopSellingProducts(@Param("limit") int limit);
}

