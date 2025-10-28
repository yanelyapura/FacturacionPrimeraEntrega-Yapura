# 📚 Primera Entrega JPA - Proyecto de Demostración

## 📋 Descripción del Proyecto

Este proyecto es una demostración completa de **JPA (Java Persistence API)** con **Spring Boot** y **Hibernate**. Implementa un sistema de gestión de pedidos (e-commerce) que incluye las mejores prácticas de modelado de datos, relaciones entre entidades y operaciones CRUD.

## 🎯 Objetivos

### Objetivos Generales
- ✅ Integrar los conocimientos de JPA y Spring Boot
- ✅ Diseñar una solución completa y funcional
- ✅ Demostrar diferentes tipos de relaciones entre entidades

### Objetivos Específicos
- ✅ Establecer entidades que representen la solución del proyecto
- ✅ Generar scripts SQL para modelar la base de datos
- ✅ Implementar repositorios JPA con consultas personalizadas
- ✅ Configurar Spring Boot para inicialización automática de BD

## 🏗️ Arquitectura del Proyecto

```
primera-entrega-jpa/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/proyecto/jpa/
│   │   │       ├── entity/              # Entidades JPA
│   │   │       │   ├── Category.java
│   │   │       │   ├── Product.java
│   │   │       │   ├── Customer.java
│   │   │       │   ├── Order.java
│   │   │       │   └── OrderItem.java
│   │   │       ├── repository/          # Repositorios JPA
│   │   │       │   ├── CategoryRepository.java
│   │   │       │   ├── ProductRepository.java
│   │   │       │   ├── CustomerRepository.java
│   │   │       │   ├── OrderRepository.java
│   │   │       │   └── OrderItemRepository.java
│   │   │       └── PrimeraEntregaJpaApplication.java
│   │   └── resources/
│   │       ├── application.properties           # Configuración principal
│   │       ├── application-mysql.properties     # Configuración MySQL
│   │       ├── schema.sql                       # Estructura de BD
│   │       └── data.sql                         # Datos iniciales
│   └── test/                            # Tests (estructura básica)
└── pom.xml                              # Dependencias Maven
```

## 📊 Modelo de Datos

### Diagrama de Relaciones

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│  Category   │◄────┐   │   Product   │         │   Customer  │
├─────────────┤     │   ├─────────────┤         ├─────────────┤
│ id (PK)     │     └───┤ category_id │     ┌───┤ id (PK)     │
│ name        │         │ id (PK)     │     │   │ first_name  │
│ description │         │ name        │     │   │ last_name   │
└─────────────┘         │ price       │     │   │ email       │
                        │ stock       │     │   │ status      │
                        └─────────────┘     │   └─────────────┘
                               ▲            │          │
                               │            │          │
                               │            │          ▼
                        ┌──────┴────────┐   │   ┌─────────────┐
                        │  OrderItem    │   │   │    Order    │
                        ├───────────────┤   │   ├─────────────┤
                        │ id (PK)       │   │   │ id (PK)     │
                        │ order_id (FK) │───┘   │ customer_id │
                        │ product_id    │◄──────┤ order_number│
                        │ quantity      │       │ status      │
                        │ unit_price    │       │ total_amount│
                        │ subtotal      │       └─────────────┘
                        └───────────────┘
```

### Entidades y Relaciones

#### 1️⃣ **Category (Categoría)**
- **Propósito**: Organizar productos en categorías
- **Relación**: `@OneToMany` con Product
- **Atributos**: id, name, description

#### 2️⃣ **Product (Producto)**
- **Propósito**: Representar productos del catálogo
- **Relaciones**: 
  - `@ManyToOne` con Category
- **Atributos**: id, name, description, price, stock, created_at, updated_at
- **Características especiales**:
  - Campo `@Transient` para datos no persistentes
  - Callbacks `@PrePersist` y `@PreUpdate` para timestamps
  - Validaciones de precio y stock positivos

#### 3️⃣ **Customer (Cliente)**
- **Propósito**: Representar clientes del sistema
- **Relación**: `@OneToMany` con Order
- **Atributos**: id, first_name, last_name, email, phone, address, birth_date, status
- **Características especiales**:
  - Email único con validación `@Email`
  - Enumeración para estados (ACTIVE, INACTIVE, SUSPENDED)
  - Validaciones con Bean Validation

#### 4️⃣ **Order (Pedido)**
- **Propósito**: Representar pedidos realizados
- **Relaciones**:
  - `@ManyToOne` con Customer
  - `@OneToMany` con OrderItem
- **Atributos**: id, order_number, order_date, status, total_amount, shipping_address
- **Características especiales**:
  - Número de orden único
  - Enumeración para estados (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
  - Método para calcular monto total automáticamente

#### 5️⃣ **OrderItem (Item de Pedido)**
- **Propósito**: Representar items individuales de cada pedido (tabla intermedia)
- **Relaciones**:
  - `@ManyToOne` con Order
  - `@ManyToOne` con Product
- **Atributos**: id, quantity, unit_price, subtotal
- **Características especiales**:
  - Almacena precio en el momento del pedido
  - Calcula subtotal automáticamente
  - Callbacks para mantener subtotal actualizado

## 🔗 Tipos de Relaciones Implementadas

### 1. One-to-Many / Many-to-One
```java
// En Category
@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
private List<Product> products;

// En Product
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

### 2. Cascadas y Orphan Removal
```java
@OneToMany(mappedBy = "order", 
           cascade = CascadeType.ALL, 
           orphanRemoval = true)
private List<OrderItem> orderItems;
```

### 3. Fetch Types
- **LAZY**: Carga bajo demanda (mejor rendimiento)
- **EAGER**: Carga inmediata (puede causar problemas N+1)

## 🗄️ Scripts SQL

### schema.sql
Define la estructura completa de la base de datos:
- ✅ 5 tablas con claves primarias auto-incrementales
- ✅ Claves foráneas con integridad referencial
- ✅ Constraints de validación (CHECK)
- ✅ Índices para optimizar consultas
- ✅ Campos únicos (email, order_number)

### data.sql
Carga datos de prueba:
- ✅ 5 categorías de productos
- ✅ 18 productos variados
- ✅ 5 clientes con diferentes estados
- ✅ 5 pedidos en distintos estados
- ✅ 14 items de pedidos

## ⚙️ Configuración

### Base de Datos H2 (Desarrollo)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
```

### Base de Datos MySQL (Producción)
```properties
# Usar perfil: -Dspring-boot.run.profiles=mysql
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_jpa
spring.jpa.hibernate.ddl-auto=update
```

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- ☕ Java 17 o superior
- 📦 Maven 3.6+
- 💻 IDE (IntelliJ IDEA, Eclipse, VS Code)

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
```bash
cd primera-entrega-jpa
```

2. **Compilar el proyecto**
```bash
mvn clean install
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

4. **Acceder a la consola H2**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (dejar en blanco)

### Ejecutar con MySQL

1. **Crear la base de datos**
```sql
CREATE DATABASE proyecto_jpa;
```

2. **Ejecutar con perfil MySQL**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## 📚 Conceptos JPA Demostrados

### ✅ Anotaciones de Entidad
- `@Entity`: Define una clase como entidad JPA
- `@Table`: Especifica el nombre de la tabla
- `@Id`: Define la clave primaria
- `@GeneratedValue`: Estrategia de generación de ID
- `@Column`: Configura columnas de la tabla

### ✅ Anotaciones de Relación
- `@OneToMany`: Relación uno a muchos
- `@ManyToOne`: Relación muchos a uno
- `@JoinColumn`: Define columna de clave foránea
- `mappedBy`: Indica el lado no propietario de la relación

### ✅ Anotaciones de Validación
- `@NotBlank`: Campo no puede estar vacío
- `@Email`: Valida formato de email
- `@Enumerated`: Mapea enumeraciones
- `@Transient`: Campo no persistente

### ✅ Callbacks de Ciclo de Vida
- `@PrePersist`: Antes de insertar
- `@PreUpdate`: Antes de actualizar
- `@PostLoad`: Después de cargar

### ✅ Configuración de Cascadas
- `CascadeType.ALL`: Propaga todas las operaciones
- `CascadeType.PERSIST`: Solo propagación de persist
- `CascadeType.REMOVE`: Solo propagación de remove
- `orphanRemoval = true`: Elimina huérfanos automáticamente

## 🔍 Repositorios JPA

### Métodos Automáticos
Todos los repositorios heredan de `JpaRepository`:
- `save(entity)`: Guardar/actualizar
- `findById(id)`: Buscar por ID
- `findAll()`: Obtener todos
- `deleteById(id)`: Eliminar por ID
- `count()`: Contar registros

### Consultas Derivadas (Query Methods)
```java
// Buscar por nombre
List<Product> findByNameContainingIgnoreCase(String name);

// Buscar por rango de precios
List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

// Buscar con stock
List<Product> findByStockGreaterThan(Integer stock);
```

### Consultas JPQL Personalizadas
```java
@Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY p.price ASC")
List<Product> findProductsByCategoryOrderByPrice(@Param("categoryId") Long categoryId);
```

### Consultas SQL Nativas
```java
@Query(value = "SELECT * FROM products WHERE price < :maxPrice", nativeQuery = true)
List<Product> findCheapProducts(@Param("maxPrice") BigDecimal maxPrice);
```

## 📖 Guía de Uso

### Ejemplo 1: Crear una Categoría y Productos
```java
// Crear categoría
Category category = new Category("Tecnología", "Productos tecnológicos");
categoryRepository.save(category);

// Crear producto
Product product = new Product("Laptop", "Laptop gaming", 
                              new BigDecimal("1299.99"), 10, category);
productRepository.save(product);
```

### Ejemplo 2: Crear un Cliente y Pedido
```java
// Crear cliente
Customer customer = new Customer("Juan", "Pérez", "juan@email.com");
customerRepository.save(customer);

// Crear pedido
Order order = new Order("ORD-001", customer);
order.setShippingAddress("Calle Principal 123");

// Agregar items al pedido
OrderItem item = new OrderItem(order, product, 2, product.getPrice());
order.addOrderItem(item);

orderRepository.save(order);
```

### Ejemplo 3: Consultas Personalizadas
```java
// Buscar productos por categoría
List<Product> products = productRepository.findByCategoryId(1L);

// Buscar pedidos de un cliente
List<Order> orders = orderRepository.findByCustomerId(1L);

// Buscar productos más vendidos
List<Product> topProducts = productRepository.findTopSellingProducts(5);
```

## 🎓 Conceptos Clave Aprendidos

1. **Entidades JPA**: Clases que se mapean a tablas
2. **Relaciones**: OneToMany, ManyToOne, ManyToMany
3. **Cascadas**: Propagación de operaciones
4. **Fetch Types**: LAZY vs EAGER
5. **Repositorios**: Interfaz para operaciones CRUD
6. **Query Methods**: Consultas por convención de nombres
7. **JPQL**: Lenguaje de consultas orientado a objetos
8. **Validaciones**: Bean Validation con anotaciones
9. **Enumeraciones**: Mapeo de tipos enum
10. **Callbacks**: Métodos de ciclo de vida

## 📝 Notas Importantes

### ⚠️ Problema N+1
Se evita usando:
- `fetch = FetchType.LAZY` (por defecto en @ManyToOne)
- `JOIN FETCH` en consultas JPQL
- `@EntityGraph` para especificar qué cargar

### 🔄 Sincronización Bidireccional
Los métodos helper mantienen la sincronización:
```java
public void addProduct(Product product) {
    products.add(product);
    product.setCategory(this);
}
```

### 🔐 hashCode() y equals()
Implementados usando la clave primaria para garantizar correcta comparación de entidades.

## 🛠️ Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación
- **Spring Boot 3.1.5**: Framework de aplicaciones
- **Spring Data JPA**: Abstracción de persistencia
- **Hibernate**: Implementación de JPA
- **H2 Database**: Base de datos en memoria (desarrollo)
- **MySQL Connector**: Driver para MySQL (producción)
- **Maven**: Gestión de dependencias
- **Bean Validation**: Validaciones declarativas

## 🎯 Puntos Destacados del Proyecto

✅ **5 entidades** con relaciones complejas  
✅ **Todos los tipos de relaciones** implementadas  
✅ **Scripts SQL completos** (schema + data)  
✅ **Repositorios JPA** con consultas personalizadas  
✅ **Validaciones** con Bean Validation  
✅ **Enumeraciones** para estados  
✅ **Callbacks** de ciclo de vida  
✅ **Configuración dual** (H2 y MySQL)  
✅ **Datos de prueba** cargados automáticamente  
✅ **Documentación completa** y comentarios  
✅ **Buenas prácticas** de diseño JPA  

## 📞 Soporte

Para más información sobre JPA y Spring Boot:
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [Java Persistence API](https://jakarta.ee/specifications/persistence/)

---

**Desarrollado como proyecto educativo para demostrar conceptos de JPA y Spring Boot** 🎓

