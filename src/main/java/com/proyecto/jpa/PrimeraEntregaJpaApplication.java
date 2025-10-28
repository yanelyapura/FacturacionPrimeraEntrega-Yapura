package com.proyecto.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot con JPA.
 * 
 * @SpringBootApplication es una anotación conveniente que combina:
 * - @Configuration: marca la clase como fuente de definiciones de beans
 * - @EnableAutoConfiguration: habilita la auto-configuración de Spring Boot
 * - @ComponentScan: escanea componentes, configuraciones y servicios en el paquete actual
 * 
 * Esta aplicación demuestra el uso de JPA (Java Persistence API) con Hibernate
 * para mapear objetos Java a tablas de base de datos relacionales.
 */
@SpringBootApplication
public class PrimeraEntregaJpaApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(PrimeraEntregaJpaApplication.class, args);
        
        System.out.println("\n==============================================");
        System.out.println("✅ Aplicación JPA iniciada correctamente");
        System.out.println("==============================================");
        System.out.println("🌐 Servidor: http://localhost:8080");
        System.out.println("🗄️  Consola H2: http://localhost:8080/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("   Usuario: sa");
        System.out.println("   Contraseña: (dejar en blanco)");
        System.out.println("==============================================\n");
    }
}

