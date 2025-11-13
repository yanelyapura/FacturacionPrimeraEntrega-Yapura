# Sistema de Facturación Empresarial

## 🚀 Demo en Vivo

**Accede a la aplicación:** [https://yanelyapura.github.io/FacturacionPrimeraEntrega-Yapura/](https://yanelyapura.github.io/FacturacionPrimeraEntrega-Yapura/)

## 📋 Descripción del Proyecto

Sistema empresarial completo de gestión de facturación desarrollado con **Java, Spring Boot y JPA**. La aplicación demuestra conceptos avanzados de persistencia de datos, relaciones entre entidades y arquitectura de microservicios.

## 🛠️ Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.1.5** - Framework de aplicaciones
- **Spring Data JPA** - Abstracción de persistencia
- **Hibernate** - Implementación de JPA
- **H2 Database** - Base de datos en memoria (desarrollo)
- **MySQL** - Base de datos de producción
- **Maven** - Gestión de dependencias
- **Bean Validation** - Validaciones declarativas

## 🏗️ Arquitectura del Sistema

### Entidades Principales

1. **Category** - Categorías de productos
2. **Product** - Catálogo de productos
3. **Customer** - Información de clientes
4. **Order** - Pedidos del sistema
5. **OrderItem** - Items de cada pedido

### Relaciones JPA

- **OneToMany**: Category → Products
- **ManyToOne**: Product → Category
- **OneToMany**: Customer → Orders
- **ManyToOne**: Order → Customer
- **OneToMany**: Order → OrderItems
- **ManyToOne**: OrderItem → Order, Product

## 🚀 Instalación Local

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Pasos de Instalación

```bash
# Clonar el repositorio
git clone https://github.com/yanelyapura/FacturacionPrimeraEntrega-Yapura.git

# Navegar al directorio
cd FacturacionPrimeraEntrega-Yapura

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Acceso a la Aplicación

- **Servidor:** http://localhost:8080
- **Consola H2:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: (dejar en blanco)

## 📊 Características Destacadas

### ✅ Conceptos JPA Implementados

- **Entidades** con anotaciones completas
- **Relaciones bidireccionales** con sincronización
- **Cascadas** y Orphan Removal
- **Fetch Types** (LAZY/EAGER)
- **Validaciones** con Bean Validation
- **Callbacks** de ciclo de vida
- **Enumeraciones** para estados

### ✅ Repositorios JPA

- **Métodos automáticos** heredados de JpaRepository
- **Query Methods** por convención de nombres
- **Consultas JPQL** personalizadas
- **Consultas SQL nativas**

### ✅ Base de Datos

- **Scripts SQL completos** (schema + data)
- **Integridad referencial** con claves foráneas
- **Índices** para optimización de consultas
- **Datos de prueba** precargados
- **Configuración dual** (H2/MySQL)

## 🔧 Configuración

### Desarrollo (H2)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
```

### Producción (MySQL)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## 📚 Documentación Técnica

- [Conceptos JPA](CONCEPTOS_JPA.md)
- [Estructura de Paquetes](ESTRUCTURA_PAQUETES.md)
- [Instrucciones de Ejecución](INSTRUCCIONES_EJECUCION.md)
- [Diagrama del Proyecto](DIAGRAMA_PROYECTO.txt)

## 👩‍💻 Desarrolladora

**Yanel Sabrina Yapura**
- Desarrolladora Fullstack
- Especializada en Java, Spring Boot, React, Python
- [GitHub](https://github.com/yanelyapura)
- [LinkedIn](https://linkedin.com/in/yanelyapura/)

## 📄 Licencia

Este proyecto fue desarrollado como demostración educativa de conceptos JPA y Spring Boot.

---

**Desarrollado con ❤️ para demostrar habilidades en Java, Spring Boot y JPA**
