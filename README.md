# Sistema de Facturación Empresarial

## 🚀 Demo en Vivo

**👉 [Ver Demo Interactivo](https://yanelyapura.github.io/FacturacionPrimeraEntrega-Yapura/home.html)**

## 📋 Descripción

Aplicación completa de gestión de facturación desarrollada con **Java, Spring Boot y JPA**. Implementación de APIs REST robustas para integración con sistemas externos.

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.1.5**
- **JPA/Hibernate**
- **MySQL/H2 Database**
- **Maven**

## 🎯 Características Principales

- **Arquitectura JPA**: 5 entidades con relaciones complejas, cascadas, y mapeo objeto-relacional completo
- **Bases de Datos**: Soporte para H2 (desarrollo) y MySQL (producción) con scripts SQL completos
- **APIs REST**: Repositorios JPA con consultas personalizadas, JPQL y métodos derivados

## 🔗 Enlaces Útiles

- **[Demo Interactivo](https://yanelyapura.github.io/FacturacionPrimeraEntrega-Yapura/home.html)** - Interfaz web completa
- **[API Demo](https://yanelyapura.github.io/FacturacionPrimeraEntrega-Yapura/api-demo.html)** - Documentación de APIs
- **[Documentación Javadoc](https://yanelyapura.github.io/FacturacionPrimeraEntrega-Yapura/index.html)** - Documentación técnica completa

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Pasos de Instalación

```bash
# Clonar el repositorio
git clone https://github.com/yanelyapura/FacturacionPrimeraEntrega-Yapura.git

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Acceso a la Aplicación

- **Servidor**: http://localhost:8080
- **Consola H2**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb
- **Usuario**: sa
- **Contraseña**: (dejar en blanco)

## 📊 Entidades del Sistema

- **📦 Categorías**: Gestión de categorías de productos con validaciones
- **🛍️ Productos**: Catálogo de productos con precios, stock y categorías
- **👥 Clientes**: Registro de clientes con información de contacto
- **📋 Pedidos**: Sistema de pedidos con estados y direcciones de envío
- **📝 Items de Pedido**: Detalle de productos en cada pedido con cantidades

## 🔧 APIs Disponibles

- **GET** `/api/categories` - Listar categorías
- **GET** `/api/products` - Listar productos
- **GET** `/api/customers` - Listar clientes
- **GET** `/api/orders` - Listar pedidos
- **POST** `/api/orders` - Crear pedido

## 👩‍💻 Desarrolladora

**Yanel Sabrina Yapura**  
Desarrolladora Fullstack

- [GitHub](https://github.com/yanelyapura)
- [LinkedIn](https://linkedin.com/in/yanelyapura/)

---

© 2025 Yanel Yapura. Desarrollado como proyecto educativo para demostrar conceptos de JPA y Spring Boot.