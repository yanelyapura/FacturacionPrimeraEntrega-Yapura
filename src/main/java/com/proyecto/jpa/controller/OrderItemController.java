package com.proyecto.jpa.controller;

import com.proyecto.jpa.entity.OrderItem;
import com.proyecto.jpa.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de items de pedidos (Capa de Presentación).
 * 
 * <p>Expone endpoints HTTP para realizar operaciones CRUD sobre items de pedidos
 * y consultar items por pedido o producto.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * OrderItemController (Capa de Presentación) ← Esta clase
 *         ↓
 * OrderItemService (Capa de Negocio)
 *         ↓
 * OrderItemRepository (Capa de Datos)
 * </pre>
 * 
 * <h2>Endpoints Disponibles</h2>
 * <ul>
 *   <li>GET /api/order-items - Obtener todos los items</li>
 *   <li>GET /api/order-items/{id} - Obtener un item por ID</li>
 *   <li>GET /api/order-items/order/{orderId} - Obtener items de un pedido</li>
 *   <li>GET /api/order-items/product/{productId} - Obtener items con un producto</li>
 *   <li>POST /api/order-items - Crear un nuevo item</li>
 *   <li>PUT /api/order-items/{id} - Actualizar un item</li>
 *   <li>PATCH /api/order-items/{id}/quantity - Actualizar cantidad</li>
 *   <li>DELETE /api/order-items/{id} - Eliminar un item</li>
 * </ul>
 * 
 * <h2>Ejemplo de Uso</h2>
 * <pre>
 * // Obtener items de un pedido
 * GET /api/order-items/order/1
 * 
 * // Crear un nuevo item
 * POST /api/order-items
 * {
 *   "order": { "id": 1 },
 *   "product": { "id": 5 },
 *   "quantity": 2,
 *   "unitPrice": 299.99
 * }
 * 
 * // Actualizar cantidad
 * PATCH /api/order-items/1/quantity?newQuantity=5
 * </pre>
 * 
 * @see OrderItem
 * @see OrderItemService
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param orderItemService servicio de items de pedidos
     */
    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Obtiene todos los items de pedidos.
     * 
     * @return lista de items con código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.findAll();
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Obtiene un item de pedido por su ID.
     * 
     * @param id el ID del item
     * @return el item con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        return orderItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los items de un pedido específico.
     * 
     * @param orderId el ID del pedido
     * @return lista de items del pedido con código 200 OK
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrder(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderItemService.findByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Obtiene todos los items que contienen un producto específico.
     * 
     * <p>Útil para ver en qué pedidos se ha incluido un producto.</p>
     * 
     * @param productId el ID del producto
     * @return lista de items con ese producto con código 200 OK
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByProduct(@PathVariable Long productId) {
        List<OrderItem> orderItems = orderItemService.findByProductId(productId);
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Crea un nuevo item de pedido.
     * 
     * <p>El subtotal se calcula automáticamente y el total del pedido se actualiza.</p>
     * 
     * @param orderItem el item a crear
     * @return el item creado con código 201 Created, o 400 Bad Request si hay errores
     */
    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) {
        try {
            OrderItem savedOrderItem = orderItemService.save(orderItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un item de pedido existente.
     * 
     * <p>El subtotal se recalcula automáticamente y el total del pedido se actualiza.</p>
     * 
     * @param id el ID del item a actualizar
     * @param orderItem los nuevos datos del item
     * @return el item actualizado con código 200 OK, o 404 Not Found si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItem orderItem) {
        
        if (!orderItemService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        orderItem.setId(id);
        try {
            OrderItem updatedOrderItem = orderItemService.save(orderItem);
            return ResponseEntity.ok(updatedOrderItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza la cantidad de un item de pedido.
     * 
     * <p>Valida que haya stock suficiente, recalcula el subtotal y actualiza
     * el total del pedido automáticamente.</p>
     * 
     * @param id el ID del item
     * @param newQuantity la nueva cantidad
     * @return el item actualizado con código 200 OK, o 400 Bad Request si hay errores
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<OrderItem> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer newQuantity) {
        try {
            OrderItem updatedOrderItem = orderItemService.updateQuantity(id, newQuantity);
            return ResponseEntity.ok(updatedOrderItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un item de pedido por su ID.
     * 
     * <p>Al eliminar el item, el total del pedido se actualiza automáticamente.</p>
     * 
     * @param id el ID del item a eliminar
     * @return código 204 No Content si se eliminó, o 404 Not Found si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        try {
            orderItemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cuenta el número total de items de pedidos.
     * 
     * @return el número de items con código 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderItems() {
        long count = orderItemService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Cuenta el número de items en un pedido específico.
     * 
     * @param orderId el ID del pedido
     * @return el número de items en ese pedido con código 200 OK
     */
    @GetMapping("/order/{orderId}/count")
    public ResponseEntity<Long> countOrderItemsByOrder(@PathVariable Long orderId) {
        long count = orderItemService.countByOrderId(orderId);
        return ResponseEntity.ok(count);
    }
}

