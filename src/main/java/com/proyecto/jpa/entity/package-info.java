/**
 * Paquete que contiene las entidades JPA del modelo de dominio.
 * 
 * <h2>Descripción</h2>
 * Este paquete contiene todas las clases de entidad que representan el modelo
 * de datos del sistema de gestión de pedidos. Cada entidad se mapea a una tabla
 * en la base de datos mediante anotaciones JPA.
 * 
 * <h2>Entidades Principales</h2>
 * <ul>
 *   <li><b>{@link com.proyecto.jpa.entity.Category}</b> - Categorías de productos</li>
 *   <li><b>{@link com.proyecto.jpa.entity.Product}</b> - Productos del catálogo</li>
 *   <li><b>{@link com.proyecto.jpa.entity.Customer}</b> - Clientes del sistema</li>
 *   <li><b>{@link com.proyecto.jpa.entity.Order}</b> - Pedidos realizados</li>
 *   <li><b>{@link com.proyecto.jpa.entity.OrderItem}</b> - Items de cada pedido</li>
 * </ul>
 * 
 * <h2>Relaciones Implementadas</h2>
 * <ul>
 *   <li><b>Category ↔ Product</b>: OneToMany / ManyToOne</li>
 *   <li><b>Customer ↔ Order</b>: OneToMany / ManyToOne</li>
 *   <li><b>Order ↔ OrderItem</b>: OneToMany / ManyToOne</li>
 *   <li><b>Product ↔ OrderItem</b>: OneToMany / ManyToOne</li>
 * </ul>
 * 
 * <h2>Características JPA Implementadas</h2>
 * <ul>
 *   <li>Anotaciones de mapeo (@Entity, @Table, @Column)</li>
 *   <li>Relaciones bidireccionales con sincronización</li>
 *   <li>Cascadas (CascadeType.ALL) y orphanRemoval</li>
 *   <li>Fetch types (LAZY y EAGER)</li>
 *   <li>Generación automática de IDs (IDENTITY)</li>
 *   <li>Validaciones con Bean Validation</li>
 *   <li>Callbacks de ciclo de vida (@PrePersist, @PreUpdate)</li>
 *   <li>Enumeraciones mapeadas (@Enumerated)</li>
 *   <li>Campos transitorios (@Transient)</li>
 * </ul>
 * 
 * @see jakarta.persistence
 * @see org.springframework.data.jpa
 * @author Proyecto JPA
 * @version 1.0.0
 */
package com.proyecto.jpa.entity;

