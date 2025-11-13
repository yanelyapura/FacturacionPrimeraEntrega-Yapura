package com.proyecto.jpa.service;

import com.proyecto.jpa.entity.OrderItem;
import com.proyecto.jpa.entity.Order;
import com.proyecto.jpa.entity.Product;
import com.proyecto.jpa.repository.OrderItemRepository;
import com.proyecto.jpa.repository.OrderRepository;
import com.proyecto.jpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de items de pedidos (Capa de Negocio).
 * 
 * <p>Implementa la lógica de negocio relacionada con los items de pedidos,
 * incluyendo validaciones de stock, cálculo de subtotales y gestión de cantidades.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * OrderItemController (Capa de Presentación)
 *         ↓
 * OrderItemService (Capa de Negocio) ← Esta clase
 *         ↓
 * OrderItemRepository (Capa de Datos)
 * </pre>
 * 
 * <h2>Responsabilidades</h2>
 * <ul>
 *   <li>Gestionar items de pedidos</li>
 *   <li>Calcular subtotales automáticamente</li>
 *   <li>Validar cantidades y precios</li>
 *   <li>Verificar disponibilidad de productos</li>
 *   <li>Actualizar totales de pedidos</li>
 * </ul>
 * 
 * @see OrderItem
 * @see OrderItemRepository
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param orderItemRepository repositorio de items de pedidos
     * @param orderRepository repositorio de pedidos
     * @param productRepository repositorio de productos
     */
    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository,
                           OrderRepository orderRepository,
                           ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Obtiene todos los items de pedidos del sistema.
     * 
     * @return lista de todos los items de pedidos
     */
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    /**
     * Busca un item de pedido por su ID.
     * 
     * @param id el ID del item de pedido
     * @return un Optional con el item si existe
     */
    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    /**
     * Busca todos los items de un pedido específico.
     * 
     * @param orderId el ID del pedido
     * @return lista de items del pedido
     */
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    /**
     * Busca todos los items que contienen un producto específico.
     * 
     * @param productId el ID del producto
     * @return lista de items con ese producto
     */
    public List<OrderItem> findByProductId(Long productId) {
        return orderItemRepository.findByProductId(productId);
    }

    /**
     * Crea o actualiza un item de pedido.
     * 
     * <p>Valida la cantidad, el precio, calcula el subtotal automáticamente
     * y actualiza el total del pedido.</p>
     * 
     * @param orderItem el item de pedido a guardar
     * @return el item de pedido guardado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    @Transactional
    public OrderItem save(OrderItem orderItem) {
        validateOrderItem(orderItem);
        
        // Calcular el subtotal antes de guardar
        orderItem.calculateSubtotal();
        
        // Guardar el item
        OrderItem savedItem = orderItemRepository.save(orderItem);
        
        // Actualizar el total del pedido
        if (orderItem.getOrder() != null) {
            updateOrderTotal(orderItem.getOrder().getId());
        }
        
        return savedItem;
    }

    /**
     * Actualiza la cantidad de un item de pedido.
     * 
     * @param orderItemId el ID del item
     * @param newQuantity la nueva cantidad
     * @return el item actualizado
     * @throws IllegalArgumentException si el item no existe o la cantidad es inválida
     */
    @Transactional
    public OrderItem updateQuantity(Long orderItemId, Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("Item de pedido no encontrado con ID: " + orderItemId));
        
        // Verificar stock disponible del producto
        if (orderItem.getProduct() != null) {
            Product product = orderItem.getProduct();
            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + product.getStock() + ", Solicitado: " + newQuantity);
            }
        }
        
        orderItem.setQuantity(newQuantity);
        orderItem.calculateSubtotal();
        
        OrderItem updatedItem = orderItemRepository.save(orderItem);
        
        // Actualizar el total del pedido
        if (orderItem.getOrder() != null) {
            updateOrderTotal(orderItem.getOrder().getId());
        }
        
        return updatedItem;
    }

    /**
     * Elimina un item de pedido por su ID.
     * 
     * <p>Al eliminar el item, se actualiza automáticamente el total del pedido.</p>
     * 
     * @param id el ID del item a eliminar
     * @throws IllegalArgumentException si no existe un item con ese ID
     */
    @Transactional
    public void deleteById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item de pedido no encontrado con ID: " + id));
        
        Long orderId = orderItem.getOrder() != null ? orderItem.getOrder().getId() : null;
        
        orderItemRepository.deleteById(id);
        
        // Actualizar el total del pedido después de eliminar el item
        if (orderId != null) {
            updateOrderTotal(orderId);
        }
    }

    /**
     * Calcula el subtotal de un item basado en cantidad y precio unitario.
     * 
     * @param quantity la cantidad
     * @param unitPrice el precio unitario
     * @return el subtotal calculado
     */
    public BigDecimal calculateSubtotal(Integer quantity, BigDecimal unitPrice) {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(new BigDecimal(quantity));
    }

    /**
     * Cuenta el número total de items de pedidos.
     * 
     * @return el número total de items
     */
    public long count() {
        return orderItemRepository.count();
    }

    /**
     * Cuenta el número de items en un pedido específico.
     * 
     * @param orderId el ID del pedido
     * @return el número de items en ese pedido
     */
    public long countByOrderId(Long orderId) {
        return findByOrderId(orderId).size();
    }

    /**
     * Valida que un item de pedido tenga datos válidos.
     * 
     * @param orderItem el item a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private void validateOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("El item de pedido no puede ser null");
        }
        
        if (orderItem.getOrder() == null || orderItem.getOrder().getId() == null) {
            throw new IllegalArgumentException("El item debe estar asociado a un pedido válido");
        }
        
        if (orderItem.getProduct() == null || orderItem.getProduct().getId() == null) {
            throw new IllegalArgumentException("El item debe tener un producto válido");
        }
        
        if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        
        if (orderItem.getUnitPrice() == null || orderItem.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
        
        // Verificar que el pedido existe
        if (!orderRepository.existsById(orderItem.getOrder().getId())) {
            throw new IllegalArgumentException("El pedido especificado no existe");
        }
        
        // Verificar que el producto existe
        if (!productRepository.existsById(orderItem.getProduct().getId())) {
            throw new IllegalArgumentException("El producto especificado no existe");
        }
        
        // Verificar stock disponible
        Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(null);
        if (product != null && product.getStock() < orderItem.getQuantity()) {
            throw new IllegalArgumentException(
                "Stock insuficiente para el producto '" + product.getName() + 
                "'. Disponible: " + product.getStock() + ", Solicitado: " + orderItem.getQuantity());
        }
    }

    /**
     * Actualiza el total de un pedido sumando todos sus items.
     * 
     * @param orderId el ID del pedido a actualizar
     */
    @Transactional
    private void updateOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.calculateTotalAmount();
            orderRepository.save(order);
        }
    }
}

