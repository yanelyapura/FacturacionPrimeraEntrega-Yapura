/**
 * Paquete que contiene los repositorios JPA para acceso a datos.
 * 
 * <h2>Descripción</h2>
 * Este paquete implementa el patrón Repository para abstraer el acceso a la
 * base de datos. Todos los repositorios extienden de {@code JpaRepository},
 * proporcionando operaciones CRUD completas sin necesidad de implementación.
 * 
 * <h2>Repositorios Disponibles</h2>
 * <ul>
 *   <li><b>{@link com.proyecto.jpa.repository.CategoryRepository}</b> 
 *       - Operaciones sobre categorías</li>
 *   <li><b>{@link com.proyecto.jpa.repository.ProductRepository}</b> 
 *       - Operaciones sobre productos</li>
 *   <li><b>{@link com.proyecto.jpa.repository.CustomerRepository}</b> 
 *       - Operaciones sobre clientes</li>
 *   <li><b>{@link com.proyecto.jpa.repository.OrderRepository}</b> 
 *       - Operaciones sobre pedidos</li>
 *   <li><b>{@link com.proyecto.jpa.repository.OrderItemRepository}</b> 
 *       - Operaciones sobre items de pedidos</li>
 * </ul>
 * 
 * <h2>Funcionalidades Implementadas</h2>
 * <ul>
 *   <li><b>Métodos CRUD automáticos</b>: save(), findById(), findAll(), delete(), etc.</li>
 *   <li><b>Query Methods</b>: Métodos generados por convención de nombres</li>
 *   <li><b>Consultas JPQL</b>: Consultas personalizadas con @Query</li>
 *   <li><b>Consultas SQL nativas</b>: Para casos específicos con nativeQuery=true</li>
 *   <li><b>Paginación y ordenamiento</b>: Heredado de PagingAndSortingRepository</li>
 * </ul>
 * 
 * <h2>Ejemplos de Uso</h2>
 * <pre>
 * // Inyectar repositorio
 * {@literal @}Autowired
 * private ProductRepository productRepository;
 * 
 * // Usar métodos automáticos
 * Product product = productRepository.findById(1L).orElse(null);
 * List&lt;Product&gt; all = productRepository.findAll();
 * 
 * // Usar query methods
 * List&lt;Product&gt; products = productRepository.findByCategoryId(1L);
 * 
 * // Usar consultas personalizadas
 * List&lt;Product&gt; topProducts = productRepository.findTopSellingProducts(5);
 * </pre>
 * 
 * <h2>Ventajas del Patrón Repository</h2>
 * <ul>
 *   <li>Abstrae la lógica de acceso a datos</li>
 *   <li>Facilita las pruebas unitarias (mockeable)</li>
 *   <li>Centraliza las consultas a la base de datos</li>
 *   <li>Reduce el código boilerplate</li>
 *   <li>Permite cambiar la implementación de persistencia fácilmente</li>
 * </ul>
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.data.jpa.repository.Query
 * @author Proyecto JPA
 * @version 1.0.0
 */
package com.proyecto.jpa.repository;

