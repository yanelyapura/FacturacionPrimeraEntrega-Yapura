package com.proyecto.jpa.repository;

import com.proyecto.jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Busca un pedido por su número de orden.
     * 
     * @param orderNumber número de orden
     * @return Optional con el pedido si existe
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Busca todos los pedidos de un cliente.
     * 
     * @param customerId ID del cliente
     * @return lista de pedidos del cliente
     */
    List<Order> findByCustomerId(Long customerId);

    /**
     * Busca pedidos por estado.
     * 
     * @param status estado del pedido
     * @return lista de pedidos con ese estado
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * Busca pedidos realizados en un rango de fechas.
     * 
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de pedidos en ese rango
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Consulta personalizada para obtener pedidos de un cliente con sus items.
     * Usa JOIN FETCH para evitar el problema N+1 (carga eager de relaciones).
     * 
     * @param customerId ID del cliente
     * @return lista de pedidos con items cargados
     */
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.orderItems " +
           "WHERE o.customer.id = :customerId")
    List<Order> findOrdersWithItemsByCustomer(@Param("customerId") Long customerId);
}

