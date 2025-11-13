package com.proyecto.jpa.controller;

import com.proyecto.jpa.entity.Order;
import com.proyecto.jpa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de pedidos (Capa de Presentación).
 * 
 * <p>Expone endpoints HTTP para realizar operaciones CRUD sobre pedidos,
 * gestionar su estado y consultar pedidos por cliente.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * OrderController (Capa de Presentación) ← Esta clase
 *         ↓
 * OrderService (Capa de Negocio)
 *         ↓
 * OrderRepository (Capa de Datos)
 * </pre>
 * 
 * @see Order
 * @see OrderService
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Obtiene todos los pedidos.
     * 
     * @return lista de pedidos con código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Obtiene un pedido por su ID.
     * 
     * @param id el ID del pedido
     * @return el pedido con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene pedidos por cliente.
     * 
     * @param customerId el ID del cliente
     * @return lista de pedidos del cliente
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.findOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Obtiene pedidos con sus items por cliente (optimizado con JOIN FETCH).
     * 
     * @param customerId el ID del cliente
     * @return lista de pedidos con items precargados
     */
    @GetMapping("/customer/{customerId}/with-items")
    public ResponseEntity<List<Order>> getOrdersWithItemsByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.findOrdersWithItemsByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Obtiene pedidos por estado.
     * 
     * @param status el estado del pedido
     * @return lista de pedidos con ese estado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.findOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Busca un pedido por su número de orden.
     * 
     * @param orderNumber el número de orden
     * @return el pedido con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return orderService.findOrderByOrderNumber(orderNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo pedido.
     * 
     * @param order el pedido a crear
     * @return el pedido creado con código 201 Created
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        try {
            Order savedOrder = orderService.saveOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un pedido existente.
     * 
     * @param id el ID del pedido a actualizar
     * @param order los nuevos datos del pedido
     * @return el pedido actualizado con código 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody Order order) {
        
        if (!orderService.findOrderById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        order.setId(id);
        try {
            Order updatedOrder = orderService.saveOrder(order);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza el estado de un pedido.
     * 
     * @param id el ID del pedido
     * @param status el nuevo estado
     * @return el pedido actualizado con código 200 OK
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancela un pedido.
     * 
     * @param id el ID del pedido a cancelar
     * @return el pedido cancelado con código 200 OK
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un pedido por su ID.
     * 
     * <p><b>Operación en Cascada:</b> Al eliminar el pedido, todos sus items
     * asociados se eliminarán automáticamente.</p>
     * 
     * @param id el ID del pedido a eliminar
     * @return código 204 No Content si se eliminó
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cuenta el número total de pedidos.
     * 
     * @return el número de pedidos con código 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrders() {
        long count = orderService.countAllOrders();
        return ResponseEntity.ok(count);
    }
}

