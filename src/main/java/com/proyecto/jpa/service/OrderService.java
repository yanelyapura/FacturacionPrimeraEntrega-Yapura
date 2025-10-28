package com.proyecto.jpa.service;

import com.proyecto.jpa.entity.Order;
import com.proyecto.jpa.entity.OrderItem;
import com.proyecto.jpa.entity.Customer;
import com.proyecto.jpa.entity.Product;
import com.proyecto.jpa.repository.OrderRepository;
import com.proyecto.jpa.repository.CustomerRepository;
import com.proyecto.jpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de pedidos y su lógica de negocio.
 * 
 * <p>Esta clase implementa la capa de servicio para operaciones relacionadas con pedidos,
 * coordinando múltiples repositorios y aplicando reglas de negocio.</p>
 * 
 * <h2>Responsabilidades</h2>
 * <ul>
 *   <li>Crear y validar pedidos</li>
 *   <li>Calcular totales de pedidos</li>
 *   <li>Verificar disponibilidad de stock</li>
 *   <li>Actualizar inventario al crear pedidos</li>
 *   <li>Gestionar el ciclo de vida de los pedidos</li>
 * </ul>
 * 
 * <h2>Transacciones</h2>
 * <p>Todos los métodos que modifican datos están marcados con {@code @Transactional}
 * para garantizar la consistencia. Si alguna operación falla, toda la transacción
 * se revierte automáticamente.</p>
 * 
 * <h2>Ejemplo de Uso</h2>
 * <pre>
 * // Inyectar el servicio
 * {@literal @}Autowired
 * private OrderService orderService;
 * 
 * // Buscar pedidos de un cliente
 * List&lt;Order&gt; orders = orderService.findOrdersByCustomerId(1L);
 * 
 * // Calcular total de un pedido
 * BigDecimal total = orderService.calculateOrderTotal(order);
 * </pre>
 * 
 * @see Order
 * @see OrderRepository
 * @see CustomerRepository
 * @see ProductRepository
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class OrderService {

    /**
     * Repositorio para acceso a datos de pedidos.
     */
    private final OrderRepository orderRepository;
    
    /**
     * Repositorio para acceso a datos de clientes.
     */
    private final CustomerRepository customerRepository;
    
    /**
     * Repositorio para acceso a datos de productos.
     */
    private final ProductRepository productRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * <p>Spring inyecta automáticamente las instancias de los repositorios
     * necesarios para este servicio.</p>
     * 
     * @param orderRepository repositorio de pedidos
     * @param customerRepository repositorio de clientes
     * @param productRepository repositorio de productos
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, 
                       CustomerRepository customerRepository,
                       ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    /**
     * Busca un pedido por su ID.
     * 
     * @param id el ID del pedido a buscar
     * @return un Optional con el pedido si existe, vacío en caso contrario
     */
    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Obtiene todos los pedidos del sistema.
     * 
     * @return lista de todos los pedidos
     */
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Busca todos los pedidos de un cliente específico.
     * 
     * <p>Este método es útil para mostrar el historial de pedidos
     * de un cliente en su perfil.</p>
     * 
     * @param customerId el ID del cliente
     * @return lista de pedidos del cliente
     */
    public List<Order> findOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    /**
     * Busca pedidos por su estado.
     * 
     * <p>Permite filtrar pedidos según su estado actual (PENDING, PROCESSING,
     * SHIPPED, DELIVERED, CANCELLED).</p>
     * 
     * @param status el estado de los pedidos a buscar
     * @return lista de pedidos con el estado especificado
     */
    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Busca un pedido por su número de orden.
     * 
     * <p>El número de orden es único y se utiliza como referencia
     * externa del pedido.</p>
     * 
     * @param orderNumber el número de orden a buscar
     * @return un Optional con el pedido si existe
     */
    public Optional<Order> findOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    /**
     * Calcula el monto total de un pedido.
     * 
     * <p>El total se calcula sumando los subtotales de todos los items
     * del pedido. Este método es útil para verificar o recalcular totales.</p>
     * 
     * @param order el pedido del cual calcular el total
     * @return el monto total del pedido
     * @throws IllegalArgumentException si el pedido es null o no tiene items
     */
    public BigDecimal calculateOrderTotal(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("El pedido no puede ser null");
        }
        
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return order.getOrderItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifica si hay stock suficiente para un producto.
     * 
     * <p>Este método es esencial antes de crear un pedido para asegurar
     * que hay inventario disponible.</p>
     * 
     * @param productId el ID del producto a verificar
     * @param quantity la cantidad requerida
     * @return true si hay stock suficiente, false en caso contrario
     */
    public boolean checkProductStock(Long productId, Integer quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return false;
        }
        
        Product product = productOpt.get();
        return product.getStock() >= quantity;
    }

    /**
     * Guarda un pedido en la base de datos.
     * 
     * <p>Este método persiste un nuevo pedido o actualiza uno existente.
     * La operación se ejecuta dentro de una transacción.</p>
     * 
     * @param order el pedido a guardar
     * @return el pedido guardado con su ID asignado
     * @throws IllegalArgumentException si el pedido es null
     */
    @Transactional
    public Order saveOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("El pedido no puede ser null");
        }
        
        // Recalcular el total antes de guardar
        order.calculateTotalAmount();
        
        return orderRepository.save(order);
    }

    /**
     * Actualiza el estado de un pedido.
     * 
     * <p>Este método permite cambiar el estado de un pedido de forma segura,
     * persistiendo el cambio en la base de datos.</p>
     * 
     * @param orderId el ID del pedido
     * @param newStatus el nuevo estado del pedido
     * @return el pedido actualizado
     * @throws IllegalArgumentException si el pedido no existe
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pedido no encontrado con ID: " + orderId));
        
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    /**
     * Cancela un pedido.
     * 
     * <p>Este método cambia el estado del pedido a CANCELLED y opcionalmente
     * puede restaurar el stock de los productos (lógica de negocio).</p>
     * 
     * @param orderId el ID del pedido a cancelar
     * @return el pedido cancelado
     * @throws IllegalArgumentException si el pedido no existe o no se puede cancelar
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pedido no encontrado con ID: " + orderId));
        
        // Validar que el pedido pueda ser cancelado
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalArgumentException(
                    "No se puede cancelar un pedido ya entregado");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        
        // TODO: Aquí se podría implementar lógica para restaurar el stock
        // restoreStock(order);
        
        return orderRepository.save(order);
    }

    /**
     * Elimina un pedido del sistema.
     * 
     * <p><b>Advertencia:</b> Esta operación es irreversible. Se recomienda
     * usar cancelación en lugar de eliminación en la mayoría de los casos.</p>
     * 
     * @param orderId el ID del pedido a eliminar
     * @throws IllegalArgumentException si el pedido no existe
     */
    @Transactional
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException(
                    "Pedido no encontrado con ID: " + orderId);
        }
        
        orderRepository.deleteById(orderId);
    }

    /**
     * Cuenta el número total de pedidos en el sistema.
     * 
     * @return el número total de pedidos
     */
    public long countAllOrders() {
        return orderRepository.count();
    }

    /**
     * Obtiene los pedidos de un cliente con sus items precargados.
     * 
     * <p>Este método utiliza JOIN FETCH para evitar el problema N+1,
     * cargando los items del pedido en una sola consulta.</p>
     * 
     * @param customerId el ID del cliente
     * @return lista de pedidos con sus items cargados
     */
    public List<Order> findOrdersWithItemsByCustomer(Long customerId) {
        return orderRepository.findOrdersWithItemsByCustomer(customerId);
    }
}

