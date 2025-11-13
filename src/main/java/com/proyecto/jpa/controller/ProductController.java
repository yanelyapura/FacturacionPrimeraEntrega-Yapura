package com.proyecto.jpa.controller;

import com.proyecto.jpa.entity.Product;
import com.proyecto.jpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para la gestión de productos (Capa de Presentación).
 * 
 * <p>Expone endpoints HTTP para realizar operaciones CRUD sobre productos
 * y consultas especializadas como búsqueda por categoría, precio, etc.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * ProductController (Capa de Presentación) ← Esta clase
 *         ↓
 * ProductService (Capa de Negocio)
 *         ↓
 * ProductRepository (Capa de Datos)
 * </pre>
 * 
 * @see Product
 * @see ProductService
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene todos los productos.
     * 
     * @return lista de productos con código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id el ID del producto
     * @return el producto con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene productos por categoría.
     * 
     * @param categoryId el ID de la categoría
     * @return lista de productos de esa categoría
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * Busca productos por nombre (búsqueda parcial).
     * 
     * @param name texto a buscar en el nombre
     * @return lista de productos que coinciden
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        List<Product> products = productService.findByNameContaining(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Obtiene productos en un rango de precios.
     * 
     * @param minPrice precio mínimo
     * @param maxPrice precio máximo
     * @return lista de productos en ese rango
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Obtiene productos disponibles (con stock).
     * 
     * @return lista de productos con stock > 0
     */
    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> products = productService.findAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Obtiene los productos más vendidos.
     * 
     * @param limit número de productos a retornar
     * @return lista de productos más vendidos
     */
    @GetMapping("/top-selling")
    public ResponseEntity<List<Product>> getTopSellingProducts(@RequestParam(defaultValue = "5") int limit) {
        List<Product> products = productService.findTopSellingProducts(limit);
        return ResponseEntity.ok(products);
    }

    /**
     * Crea un nuevo producto.
     * 
     * @param product el producto a crear
     * @return el producto creado con código 201 Created
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un producto existente.
     * 
     * @param id el ID del producto a actualizar
     * @param product los nuevos datos del producto
     * @return el producto actualizado con código 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        
        if (!productService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        product.setId(id);
        try {
            Product updatedProduct = productService.save(product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza el stock de un producto.
     * 
     * @param id el ID del producto
     * @param newStock el nuevo valor de stock
     * @return el producto actualizado con código 200 OK
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long id,
            @RequestParam Integer newStock) {
        try {
            Product updatedProduct = productService.updateStock(id, newStock);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza el precio de un producto.
     * 
     * @param id el ID del producto
     * @param newPrice el nuevo precio
     * @return el producto actualizado con código 200 OK
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<Product> updatePrice(
            @PathVariable Long id,
            @RequestParam BigDecimal newPrice) {
        try {
            Product updatedProduct = productService.updatePrice(id, newPrice);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id el ID del producto a eliminar
     * @return código 204 No Content si se eliminó
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cuenta el número total de productos.
     * 
     * @return el número de productos con código 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProducts() {
        long count = productService.count();
        return ResponseEntity.ok(count);
    }
}

