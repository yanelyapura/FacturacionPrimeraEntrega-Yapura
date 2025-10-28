package com.proyecto.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Clase de configuración para la base de datos y JPA.
 * 
 * <p>Esta clase configura los aspectos relacionados con la persistencia de datos,
 * incluyendo la habilitación de repositorios JPA y la gestión de transacciones.</p>
 * 
 * <h2>Funcionalidades Configuradas</h2>
 * <ul>
 *   <li><b>Repositorios JPA</b>: Escaneo automático en el paquete repository</li>
 *   <li><b>Gestión de Transacciones</b>: Soporte para @Transactional</li>
 *   <li><b>EntityManager</b>: Gestión automática del contexto de persistencia</li>
 * </ul>
 * 
 * <h2>Anotaciones Principales</h2>
 * <ul>
 *   <li><b>@Configuration</b>: Indica que es una clase de configuración de Spring</li>
 *   <li><b>@EnableJpaRepositories</b>: Habilita la detección automática de repositorios JPA</li>
 *   <li><b>@EnableTransactionManagement</b>: Habilita el soporte de transacciones declarativas</li>
 * </ul>
 * 
 * <h2>Configuración de Repositorios</h2>
 * <p>Los repositorios JPA se escanean automáticamente en el paquete 
 * {@code com.proyecto.jpa.repository}. Cualquier interfaz que extienda 
 * {@code JpaRepository} será detectada y se creará una implementación automáticamente.</p>
 * 
 * <h2>Gestión de Transacciones</h2>
 * <p>Con {@code @EnableTransactionManagement}, todos los métodos anotados con
 * {@code @Transactional} se ejecutarán dentro de una transacción. Si el método
 * se completa sin errores, se hace commit; si hay una excepción, se hace rollback.</p>
 * 
 * <h2>Ejemplo de Uso de Transacciones</h2>
 * <pre>
 * {@literal @}Service
 * public class OrderService {
 *     
 *     {@literal @}Transactional
 *     public void createOrder(Order order) {
 *         // Si algo falla aquí, se hace rollback automáticamente
 *         orderRepository.save(order);
 *         // ...otras operaciones
 *     }
 * }
 * </pre>
 * 
 * <h2>Propiedades de Configuración</h2>
 * <p>Las propiedades específicas de la base de datos se definen en 
 * {@code application.properties}:</p>
 * <ul>
 *   <li>spring.datasource.url - URL de conexión</li>
 *   <li>spring.jpa.hibernate.ddl-auto - Estrategia de generación del esquema</li>
 *   <li>spring.jpa.show-sql - Mostrar consultas SQL en logs</li>
 *   <li>spring.sql.init.mode - Modo de inicialización de scripts SQL</li>
 * </ul>
 * 
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @see org.springframework.transaction.annotation.EnableTransactionManagement
 * @see org.springframework.context.annotation.Configuration
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.proyecto.jpa.repository")
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * Constructor por defecto.
     * 
     * <p>Spring Boot se encarga automáticamente de crear el DataSource,
     * EntityManagerFactory y TransactionManager basándose en las propiedades
     * definidas en application.properties.</p>
     */
    public DatabaseConfig() {
        // Spring Boot auto-configura todos los beans necesarios
        // No se requiere configuración manual para H2
    }
    
    // Nota: Para configuraciones más avanzadas, se pueden definir beans personalizados aquí:
    
    /**
     * Ejemplo de bean personalizado para DataSource (comentado).
     * 
     * <p>Descomenta y modifica este método si necesitas configuración personalizada
     * del DataSource.</p>
     * 
     * @return DataSource configurado
     */
    /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
    */
    
    /**
     * Ejemplo de bean personalizado para LocalContainerEntityManagerFactoryBean (comentado).
     * 
     * <p>Descomenta y modifica este método si necesitas configuración avanzada
     * del EntityManagerFactory.</p>
     * 
     * @param dataSource el DataSource a usar
     * @return LocalContainerEntityManagerFactoryBean configurado
     */
    /*
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.proyecto.jpa.entity");
        
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        em.setJpaProperties(properties);
        
        return em;
    }
    */
}

