package com.proyecto.jpa.repository;

import com.proyecto.jpa.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad OrderItem.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Busca todos los items de un pedido.
     * 
     * @param orderId ID del pedido
     * @return lista de items del pedido
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Busca todos los items que contienen un producto específico.
     * 
     * @param productId ID del producto
     * @return lista de items que contienen ese producto
     */
    List<OrderItem> findByProductId(Long productId);
}

