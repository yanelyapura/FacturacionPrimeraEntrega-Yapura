/**
 * Paquete que contiene las clases de configuración de Spring Boot.
 * 
 * <h2>Descripción</h2>
 * Este paquete contiene las clases de configuración que personalizan el
 * comportamiento de Spring Boot y sus componentes. Aquí se definen beans,
 * configuraciones de base de datos, seguridad, y otros aspectos del sistema.
 * 
 * <h2>Clases de Configuración</h2>
 * <ul>
 *   <li><b>{@link com.proyecto.jpa.config.DatabaseConfig}</b> 
 *       - Configuración de base de datos y JPA</li>
 * </ul>
 * 
 * <h2>Anotaciones Utilizadas</h2>
 * <ul>
 *   <li><b>@Configuration</b>: Marca la clase como fuente de beans</li>
 *   <li><b>@Bean</b>: Define un bean gestionado por Spring</li>
 *   <li><b>@EnableJpaRepositories</b>: Habilita repositorios JPA</li>
 *   <li><b>@EnableTransactionManagement</b>: Habilita gestión de transacciones</li>
 * </ul>
 * 
 * <h2>Propósito de las Configuraciones</h2>
 * <ul>
 *   <li>Centralizar la configuración del sistema</li>
 *   <li>Evitar hardcodeo en las clases de negocio</li>
 *   <li>Facilitar cambios de configuración</li>
 *   <li>Separar responsabilidades (Separation of Concerns)</li>
 *   <li>Permitir diferentes configuraciones por entorno (dev, test, prod)</li>
 * </ul>
 * 
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.context.annotation.Bean
 * @author Proyecto JPA
 * @version 1.0.0
 */
package com.proyecto.jpa.config;

