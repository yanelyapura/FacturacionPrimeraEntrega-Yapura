package com.proyecto.jpa.service;

import com.proyecto.jpa.entity.Product;
import com.proyecto.jpa.entity.Category;
import com.proyecto.jpa.repository.ProductRepository;
import com.proyecto.jpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de productos (Capa de Negocio).
 * 
 * <p>Implementa la lógica de negocio relacionada con los productos,
 * incluyendo validaciones de stock, precios y relaciones con categorías.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * ProductController (Capa de Presentación)
 *         ↓
 * ProductService (Capa de Negocio) ← Esta clase
 *         ↓
 * ProductRepository (Capa de Datos)
 * </pre>
 * 
 * @see Product
 * @see ProductRepository
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param productRepository repositorio de productos
     * @param categoryRepository repositorio de categorías
     */
    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Obtiene todos los productos del sistema.
     * 
     * @return lista de todos los productos
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su ID.
     * 
     * @param id el ID del producto
     * @return un Optional con el producto si existe
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Busca productos por categoría.
     * 
     * @param categoryId el ID de la categoría
     * @return lista de productos de esa categoría
     */
    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Busca productos por nombre (búsqueda parcial, case-insensitive).
     * 
     * @param name texto a buscar en el nombre
     * @return lista de productos que coinciden
     */
    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Busca productos en un rango de precios.
     * 
     * @param minPrice precio mínimo
     * @param maxPrice precio máximo
     * @return lista de productos en ese rango
     */
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Busca productos con stock disponible.
     * 
     * @return lista de productos con stock mayor a 0
     */
    public List<Product> findAvailableProducts() {
        return productRepository.findByStockGreaterThan(0);
    }

    /**
     * Obtiene los productos más vendidos.
     * 
     * @param limit número de productos a retornar
     * @return lista de productos más vendidos
     */
    public List<Product> findTopSellingProducts(int limit) {
        return productRepository.findTopSellingProducts(limit);
    }

    /**
     * Crea o actualiza un producto.
     * 
     * <p>Valida que el producto tenga nombre, precio positivo, stock no negativo
     * y que la categoría exista.</p>
     * 
     * @param product el producto a guardar
     * @return el producto guardado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    @Transactional
    public Product save(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    /**
     * Actualiza el stock de un producto.
     * 
     * @param productId el ID del producto
     * @param newStock el nuevo valor de stock
     * @return el producto actualizado
     * @throws IllegalArgumentException si el producto no existe o el stock es negativo
     */
    @Transactional
    public Product updateStock(Long productId, Integer newStock) {
        if (newStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));
        
        product.setStock(newStock);
        return productRepository.save(product);
    }

    /**
     * Actualiza el precio de un producto.
     * 
     * @param productId el ID del producto
     * @param newPrice el nuevo precio
     * @return el producto actualizado
     * @throws IllegalArgumentException si el producto no existe o el precio es inválido
     */
    @Transactional
    public Product updatePrice(Long productId, BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));
        
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id el ID del producto a eliminar
     * @throws IllegalArgumentException si no existe un producto con ese ID
     */
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un producto con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Cuenta el número total de productos.
     * 
     * @return el número total de productos
     */
    public long count() {
        return productRepository.count();
    }

    /**
     * Valida que un producto tenga datos válidos.
     * 
     * @param product el producto a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener una categoría válida");
        }
        
        // Verificar que la categoría exista
        if (!categoryRepository.existsById(product.getCategory().getId())) {
            throw new IllegalArgumentException("La categoría especificada no existe");
        }
    }
}

