/**
 * Paquete que contiene la capa de servicios (lógica de negocio).
 * 
 * <h2>Descripción</h2>
 * Este paquete implementa el patrón Service Layer, separando la lógica de negocio
 * de la lógica de presentación y acceso a datos. Los servicios coordinan operaciones
 * complejas que pueden involucrar múltiples repositorios y validaciones de negocio.
 * 
 * <h2>Servicios Disponibles</h2>
 * <ul>
 *   <li><b>{@link com.proyecto.jpa.service.OrderService}</b> 
 *       - Gestión de lógica de negocio de pedidos</li>
 * </ul>
 * 
 * <h2>Responsabilidades de la Capa de Servicio</h2>
 * <ul>
 *   <li><b>Lógica de Negocio</b>: Implementa reglas de negocio complejas</li>
 *   <li><b>Coordinación</b>: Orquesta múltiples repositorios</li>
 *   <li><b>Validación</b>: Valida datos antes de persistir</li>
 *   <li><b>Transacciones</b>: Define límites transaccionales con @Transactional</li>
 *   <li><b>Transformación</b>: Convierte entre entidades y DTOs</li>
 * </ul>
 * 
 * <h2>Patrón Service Layer</h2>
 * <p>El patrón Service Layer proporciona una interfaz de alto nivel para
 * operaciones de negocio, encapsulando la lógica compleja y promoviendo
 * la reutilización de código.</p>
 * 
 * <h2>Arquitectura en Capas</h2>
 * <pre>
 * Controller/API (Presentación)
 *        ↓
 *    Service (Lógica de Negocio) ← Esta capa
 *        ↓
 *  Repository (Acceso a Datos)
 *        ↓
 *    Database (Persistencia)
 * </pre>
 * 
 * <h2>Ejemplo de Uso</h2>
 * <pre>
 * {@literal @}RestController
 * {@literal @}RequestMapping("/api/orders")
 * public class OrderController {
 *     
 *     {@literal @}Autowired
 *     private OrderService orderService;
 *     
 *     {@literal @}PostMapping
 *     public Order createOrder({@literal @}RequestBody OrderDTO orderDTO) {
 *         // El controlador delega la lógica al servicio
 *         return orderService.createOrder(orderDTO);
 *     }
 * }
 * </pre>
 * 
 * <h2>Ventajas del Patrón Service</h2>
 * <ul>
 *   <li>Separa responsabilidades (Separation of Concerns)</li>
 *   <li>Facilita el testing (se puede mockear fácilmente)</li>
 *   <li>Centraliza la lógica de negocio</li>
 *   <li>Permite reutilización de operaciones complejas</li>
 *   <li>Simplifica los controladores</li>
 * </ul>
 * 
 * <h2>Anotaciones Comunes</h2>
 * <ul>
 *   <li><b>@Service</b>: Marca la clase como un servicio de Spring</li>
 *   <li><b>@Transactional</b>: Define límites transaccionales</li>
 *   <li><b>@Autowired</b>: Inyecta dependencias automáticamente</li>
 * </ul>
 * 
 * @see org.springframework.stereotype.Service
 * @see org.springframework.transaction.annotation.Transactional
 * @author Proyecto JPA
 * @version 1.0.0
 */
package com.proyecto.jpa.service;

