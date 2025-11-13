/**
 * Paquete que contiene los controladores REST (Capa de Presentación).
 * 
 * <h2>Descripción</h2>
 * Este paquete implementa la capa de presentación mediante controladores REST
 * que exponen endpoints HTTP para interactuar con el sistema. Los controladores
 * reciben peticiones HTTP, delegan la lógica de negocio a los servicios y
 * retornan respuestas en formato JSON.
 * 
 * <h2>Controladores Disponibles</h2>
 * <ul>
 *   <li><b>{@link com.proyecto.jpa.controller.CategoryController}</b> 
 *       - API REST para categorías</li>
 *   <li><b>{@link com.proyecto.jpa.controller.ProductController}</b> 
 *       - API REST para productos</li>
 *   <li><b>{@link com.proyecto.jpa.controller.CustomerController}</b> 
 *       - API REST para clientes</li>
 *   <li><b>{@link com.proyecto.jpa.controller.OrderController}</b> 
 *       - API REST para pedidos</li>
 * </ul>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * Controller (Presentación) ← Esta capa
 *     ↓
 * Service (Lógica de Negocio)
 *     ↓
 * Repository (Acceso a Datos)
 *     ↓
 * Database (Persistencia)
 * </pre>
 * 
 * <h2>Responsabilidades de los Controladores</h2>
 * <ul>
 *   <li><b>Recibir peticiones HTTP</b>: GET, POST, PUT, DELETE</li>
 *   <li><b>Validar datos de entrada</b>: Usando Bean Validation</li>
 *   <li><b>Delegar a servicios</b>: No contienen lógica de negocio</li>
 *   <li><b>Manejar respuestas</b>: Códigos HTTP y formato JSON</li>
 *   <li><b>Manejo de errores</b>: Traducir excepciones a respuestas HTTP</li>
 * </ul>
 * 
 * <h2>Endpoints REST</h2>
 * <p>Todos los controladores siguen las convenciones RESTful:</p>
 * <ul>
 *   <li><b>GET /api/{recurso}</b> - Obtener todos</li>
 *   <li><b>GET /api/{recurso}/{id}</b> - Obtener uno por ID</li>
 *   <li><b>POST /api/{recurso}</b> - Crear nuevo</li>
 *   <li><b>PUT /api/{recurso}/{id}</b> - Actualizar existente</li>
 *   <li><b>DELETE /api/{recurso}/{id}</b> - Eliminar</li>
 * </ul>
 * 
 * <h2>Códigos de Estado HTTP</h2>
 * <ul>
 *   <li><b>200 OK</b>: Operación exitosa</li>
 *   <li><b>201 Created</b>: Recurso creado exitosamente</li>
 *   <li><b>204 No Content</b>: Eliminación exitosa</li>
 *   <li><b>400 Bad Request</b>: Datos inválidos</li>
 *   <li><b>404 Not Found</b>: Recurso no encontrado</li>
 *   <li><b>500 Internal Server Error</b>: Error del servidor</li>
 * </ul>
 * 
 * <h2>Ejemplo de Uso</h2>
 * <pre>
 * // GET - Obtener todos los productos
 * GET http://localhost:8080/api/products
 * 
 * // GET - Obtener un producto por ID
 * GET http://localhost:8080/api/products/1
 * 
 * // POST - Crear un nuevo producto
 * POST http://localhost:8080/api/products
 * Content-Type: application/json
 * {
 *   "name": "Nuevo Producto",
 *   "price": 99.99,
 *   "stock": 10,
 *   "categoryId": 1
 * }
 * 
 * // PUT - Actualizar un producto
 * PUT http://localhost:8080/api/products/1
 * Content-Type: application/json
 * {
 *   "name": "Producto Actualizado",
 *   "price": 149.99,
 *   "stock": 20
 * }
 * 
 * // DELETE - Eliminar un producto
 * DELETE http://localhost:8080/api/products/1
 * </pre>
 * 
 * <h2>Anotaciones Principales</h2>
 * <ul>
 *   <li><b>@RestController</b>: Marca la clase como controlador REST</li>
 *   <li><b>@RequestMapping</b>: Define la ruta base del controlador</li>
 *   <li><b>@GetMapping</b>: Mapea peticiones GET</li>
 *   <li><b>@PostMapping</b>: Mapea peticiones POST</li>
 *   <li><b>@PutMapping</b>: Mapea peticiones PUT</li>
 *   <li><b>@DeleteMapping</b>: Mapea peticiones DELETE</li>
 *   <li><b>@PathVariable</b>: Extrae variables de la URL</li>
 *   <li><b>@RequestBody</b>: Mapea el cuerpo de la petición a un objeto</li>
 *   <li><b>@Valid</b>: Valida el objeto automáticamente</li>
 * </ul>
 * 
 * <h2>Modificaciones en Cascada</h2>
 * <p>Las operaciones se propagan automáticamente gracias a la configuración
 * de cascadas en las entidades JPA. Por ejemplo, al eliminar una categoría,
 * se eliminan automáticamente todos sus productos asociados.</p>
 * 
 * @see org.springframework.web.bind.annotation.RestController
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @author Proyecto JPA
 * @version 1.0.0
 */
package com.proyecto.jpa.controller;

