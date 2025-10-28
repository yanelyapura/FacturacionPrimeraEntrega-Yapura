/**
 * Paquete raíz de la aplicación JPA para gestión de pedidos.
 * 
 * <h2>Descripción del Proyecto</h2>
 * Este proyecto implementa un sistema completo de gestión de pedidos (e-commerce)
 * utilizando JPA (Java Persistence API) con Hibernate como proveedor de persistencia
 * y Spring Boot como framework de aplicación.
 * 
 * <h2>Estructura de Paquetes</h2>
 * <ul>
 *   <li><b>entity</b> - Entidades JPA que se mapean a tablas de base de datos</li>
 *   <li><b>repository</b> - Interfaces de repositorio para acceso a datos</li>
 *   <li><b>service</b> - Capa de lógica de negocio</li>
 *   <li><b>config</b> - Clases de configuración de Spring</li>
 * </ul>
 * 
 * <h2>Tecnologías Utilizadas</h2>
 * <ul>
 *   <li>Java 17</li>
 *   <li>Spring Boot 3.1.5</li>
 *   <li>Spring Data JPA</li>
 *   <li>Hibernate 6.2.13</li>
 *   <li>H2 Database (desarrollo)</li>
 *   <li>MySQL (producción opcional)</li>
 * </ul>
 * 
 * <h2>Patrones de Diseño Implementados</h2>
 * <ul>
 *   <li><b>Repository Pattern</b> - Abstracción del acceso a datos</li>
 *   <li><b>Service Layer Pattern</b> - Separación de lógica de negocio</li>
 *   <li><b>ORM Pattern</b> - Mapeo objeto-relacional con JPA</li>
 *   <li><b>Dependency Injection</b> - Inyección de dependencias con Spring</li>
 * </ul>
 * 
 * @author Proyecto JPA - Primera Entrega
 * @version 1.0.0
 * @since 2024
 */
package com.proyecto.jpa;

