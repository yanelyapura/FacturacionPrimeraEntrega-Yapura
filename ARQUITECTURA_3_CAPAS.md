# 🏗️ Arquitectura de 3 Capas - Proyecto JPA Spring Boot

## 📋 Índice
1. [Visión General](#visión-general)
2. [Arquitectura Implementada](#arquitectura-implementada)
3. [Capa de Presentación (Controller)](#capa-de-presentación-controller)
4. [Capa de Negocio (Service)](#capa-de-negocio-service)
5. [Capa de Datos (Repository)](#capa-de-datos-repository)
6. [Modificaciones en Cascada](#modificaciones-en-cascada)
7. [Scripts de Inicialización](#scripts-de-inicialización)
8. [Ejemplos de Uso](#ejemplos-de-uso)

---

## 🎯 Visión General

Este proyecto implementa una **arquitectura de 3 capas** completa para todas las entidades JPA, siguiendo las mejores prácticas de diseño de software empresarial.

###

 Arquitectura Implementada

```
┌─────────────────────────────────────────────┐
│     CAPA DE PRESENTACIÓN (Controller)      │
│           API REST - JSON                   │
│  • CategoryController                       │
│  • ProductController                        │
│  • CustomerController                       │
│  • OrderController                          │
└────────────────┬────────────────────────────┘
                 │ HTTP Requests/Responses
                 ▼
┌─────────────────────────────────────────────┐
│      CAPA DE NEGOCIO (Service)              │
│         Lógica de Negocio                   │
│  • CategoryService                          │
│  • ProductService                           │
│  • CustomerService                          │
│  • OrderService                             │
└────────────────┬────────────────────────────┘
                 │ Llamadas a métodos
                 ▼
┌─────────────────────────────────────────────┐
│       CAPA DE DATOS (Repository)            │
│         Acceso a Datos - JPA                │
│  • CategoryRepository                       │
│  • ProductRepository                        │
│  • CustomerRepository                       │
│  • OrderRepository                          │
│  • OrderItemRepository                      │
└────────────────┬────────────────────────────┘
                 │ SQL Queries
                 ▼
┌─────────────────────────────────────────────┐
│          BASE DE DATOS (H2/MySQL)           │
│  • categories                               │
│  • products                                 │
│  • customers                                │
│  • orders                                   │
│  • order_items                              │
└─────────────────────────────────────────────┘
```

---

## 🎨 Capa de Presentación (Controller)

### Responsabilidades
✅ Recibir peticiones HTTP (GET, POST, PUT, DELETE)  
✅ Validar datos de entrada con Bean Validation  
✅ Delegar lógica de negocio a los servicios  
✅ Retornar respuestas HTTP con códigos apropiados  
✅ Manejar errores y excepciones  

### Controladores Implementados

| Controlador | Ruta Base | Entidad |
|-------------|-----------|---------|
| `CategoryController` | `/api/categories` | Category |
| `ProductController` | `/api/products` | Product |
| `CustomerController` | `/api/customers` | Customer |
| `OrderController` | `/api/orders` | Order |

### Endpoints Disponibles

#### CategoryController (`/api/categories`)
```http
GET    /api/categories              # Obtener todas
GET    /api/categories/{id}         # Obtener por ID
POST   /api/categories              # Crear nueva
PUT    /api/categories/{id}         # Actualizar
DELETE /api/categories/{id}         # Eliminar (en cascada)
GET    /api/categories/count        # Contar total
```

#### ProductController (`/api/products`)
```http
GET    /api/products                      # Obtener todos
GET    /api/products/{id}                 # Obtener por ID
GET    /api/products/category/{id}        # Por categoría
GET    /api/products/search?name=...      # Buscar por nombre
GET    /api/products/price-range?min=&max=  # Por rango de precio
GET    /api/products/available            # Con stock
GET    /api/products/top-selling?limit=5  # Más vendidos
POST   /api/products                      # Crear nuevo
PUT    /api/products/{id}                 # Actualizar
PATCH  /api/products/{id}/stock?newStock= # Actualizar stock
PATCH  /api/products/{id}/price?newPrice= # Actualizar precio
DELETE /api/products/{id}                 # Eliminar
GET    /api/products/count                # Contar total
```

#### CustomerController (`/api/customers`)
```http
GET    /api/customers                    # Obtener todos
GET    /api/customers/{id}               # Obtener por ID
GET    /api/customers/email/{email}      # Por email
GET    /api/customers/active             # Solo activos
GET    /api/customers/status/{status}    # Por estado
GET    /api/customers/search?searchTerm= # Buscar por nombre
POST   /api/customers                    # Crear nuevo
PUT    /api/customers/{id}               # Actualizar
PATCH  /api/customers/{id}/status?status= # Actualizar estado
POST   /api/customers/{id}/suspend       # Suspender
POST   /api/customers/{id}/activate      # Activar
DELETE /api/customers/{id}               # Eliminar (en cascada)
GET    /api/customers/count              # Contar total
```

#### OrderController (`/api/orders`)
```http
GET    /api/orders                           # Obtener todos
GET    /api/orders/{id}                      # Obtener por ID
GET    /api/orders/customer/{customerId}     # Por cliente
GET    /api/orders/customer/{customerId}/with-items  # Con items
GET    /api/orders/status/{status}           # Por estado
GET    /api/orders/number/{orderNumber}      # Por número
POST   /api/orders                           # Crear nuevo
PUT    /api/orders/{id}                      # Actualizar
PATCH  /api/orders/{id}/status?status=       # Actualizar estado
POST   /api/orders/{id}/cancel               # Cancelar
DELETE /api/orders/{id}                      # Eliminar (en cascada)
GET    /api/orders/count                     # Contar total
```

---

## 🧠 Capa de Negocio (Service)

### Responsabilidades
✅ Implementar lógica de negocio y reglas  
✅ Validar datos antes de persistir  
✅ Coordinar múltiples repositorios  
✅ Gestionar transacciones con `@Transactional`  
✅ Transformar y calcular datos  

### Servicios Implementados

| Servicio | Responsabilidad Principal |
|----------|--------------------------|
| `CategoryService` | Gestionar categorías y validar unicidad de nombres |
| `ProductService` | Gestionar productos, stock, precios y relaciones con categorías |
| `CustomerService` | Gestionar clientes, estados y validar emails únicos |
| `OrderService` | Gestionar pedidos, calcular totales, verificar stock |

### Características de los Servicios

#### Transaccionalidad
```java
@Service
@Transactional(readOnly = true)  // Por defecto, solo lectura
public class ProductService {
    
    @Transactional  // Escritura cuando sea necesario
    public Product save(Product product) {
        // Si algo falla, se hace rollback automáticamente
        return productRepository.save(product);
    }
}
```

#### Validaciones
```java
private void validateProduct(Product product) {
    if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("El precio debe ser mayor a cero");
    }
    // Más validaciones...
}
```

---

## 💾 Capa de Datos (Repository)

### Responsabilidades
✅ Abstraer el acceso a la base de datos  
✅ Proporcionar operaciones CRUD automáticas  
✅ Definir consultas personalizadas  
✅ Usar Spring Data JPA para generar implementaciones  

### Repositorios Implementados

| Repositorio | Entidad | Métodos Personalizados |
|-------------|---------|------------------------|
| `CategoryRepository` | Category | findByName, existsByName |
| `ProductRepository` | Product | findByCategoryId, findByNameContaining, findByPriceBetween, findTopSellingProducts |
| `CustomerRepository` | Customer | findByEmail, findByStatus, existsByEmail |
| `OrderRepository` | Order | findByCustomerId, findByStatus, findOrdersWithItemsByCustomer |
| `OrderItemRepository` | OrderItem | findByOrderId, findByProductId |

### Tipos de Consultas

#### Query Methods (Generadas automáticamente)
```java
List<Product> findByCategoryId(Long categoryId);
List<Product> findByNameContainingIgnoreCase(String name);
```

#### Consultas JPQL
```java
@Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY p.price ASC")
List<Product> findProductsByCategoryOrderByPrice(@Param("categoryId") Long categoryId);
```

#### Consultas SQL Nativas
```java
@Query(value = "SELECT * FROM products WHERE price < :maxPrice", nativeQuery = true)
List<Product> findCheapProducts(@Param("maxPrice") BigDecimal maxPrice);
```

---

## 🔄 Modificaciones en Cascada

Las modificaciones en cascada están configuradas en las entidades JPA mediante `cascade = CascadeType.ALL`.

### Configuración en las Entidades

#### Category → Products
```java
@Entity
public class Category {
    @OneToMany(mappedBy = "category", 
               cascade = CascadeType.ALL,    // ← Cascada configurada
               orphanRemoval = true)         // ← Eliminar huérfanos
    private List<Product> products;
}
```

**Efecto:** Al eliminar una categoría, todos sus productos se eliminan automáticamente.

#### Customer → Orders
```java
@Entity
public class Customer {
    @OneToMany(mappedBy = "customer", 
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Order> orders;
}
```

**Efecto:** Al eliminar un cliente, todos sus pedidos se eliminan automáticamente.

#### Order → OrderItems
```java
@Entity
public class Order {
    @OneToMany(mappedBy = "order", 
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<OrderItem> orderItems;
}
```

**Efecto:** Al eliminar un pedido, todos sus items se eliminan automáticamente.

### Demostración de Cascada

```java
// Al eliminar una categoría
categoryService.deleteById(1L);

// SQL generado automáticamente:
// DELETE FROM order_items WHERE order_id IN (SELECT o.id FROM orders o WHERE product_id IN (SELECT p.id FROM products p WHERE category_id = 1));
// DELETE FROM products WHERE category_id = 1;
// DELETE FROM categories WHERE category_id = 1;
```

### Tipos de Cascada

| CascadeType | Descripción |
|-------------|-------------|
| `ALL` | Propaga todas las operaciones |
| `PERSIST` | Solo al guardar |
| `MERGE` | Solo al actualizar |
| `REMOVE` | Solo al eliminar |
| `REFRESH` | Solo al refrescar |
| `DETACH` | Solo al desasociar |

---

## 📜 Scripts de Inicialización

### Schema.sql - Estructura de la Base de Datos

**Ubicación:** `src/main/resources/schema.sql`

**Contenido:**
- Creación de 5 tablas con relaciones
- Claves primarias auto-incrementales
- Claves foráneas con integridad referencial
- Constraints de validación (CHECK)
- Índices para optimizar consultas

```sql
CREATE TABLE categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    CONSTRAINT uk_category_name UNIQUE (name)
);

CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES categories(category_id) ON DELETE CASCADE,
    CONSTRAINT chk_product_price CHECK (price >= 0)
);

-- ... más tablas
```

### Data.sql - Datos Iniciales

**Ubicación:** `src/main/resources/data.sql`

**Contenido:**
- 5 categorías de productos
- 18 productos distribuidos en categorías
- 5 clientes con diferentes estados
- 5 pedidos en varios estados
- 14 items de pedidos

```sql
INSERT INTO categories (name, description) VALUES
('Electrónica', 'Dispositivos electrónicos y accesorios'),
('Ropa', 'Prendas de vestir para todas las edades'),
-- ... más categorías

INSERT INTO products (name, price, stock, category_id) VALUES
('Laptop HP Pavilion', 899.99, 15, 1),
('Mouse Logitech', 79.99, 50, 1),
-- ... más productos
```

### Configuración de Inicialización

En `application.properties`:

```properties
# Habilitar inicialización con scripts SQL
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql

# Deshabilitar auto-generación de Hibernate
spring.jpa.hibernate.ddl-auto=none
```

---

## 🚀 Ejemplos de Uso

### Ejemplo 1: Crear una Categoría (Con Cascada)

#### 1. Request HTTP
```http
POST /api/categories
Content-Type: application/json

{
  "name": "Deportes",
  "description": "Artículos deportivos"
}
```

#### 2. Flujo a través de las capas
```
Controller → Service → Repository → Database
```

#### 3. Response
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 6,
  "name": "Deportes",
  "description": "Artículos deportivos",
  "products": []
}
```

### Ejemplo 2: Crear un Producto (Relacionado con Categoría)

#### 1. Request
```http
POST /api/products
Content-Type: application/json

{
  "name": "Pelota de Fútbol",
  "description": "Pelota oficial FIFA",
  "price": 49.99,
  "stock": 100,
  "category": {
    "id": 6
  }
}
```

#### 2. El servicio valida:
- Precio positivo ✓
- Stock no negativo ✓
- Categoría existe ✓

#### 3. Response
```http
HTTP/1.1 201 Created

{
  "id": 19,
  "name": "Pelota de Fútbol",
  "price": 49.99,
  "stock": 100,
  "category": {
    "id": 6,
    "name": "Deportes"
  }
}
```

### Ejemplo 3: Eliminar Categoría (Con Cascada)

#### 1. Request
```http
DELETE /api/categories/6
```

#### 2. Operación en Cascada
```
1. Elimina la categoría (id=6)
   ↓
2. Automáticamente elimina todos los productos con category_id=6
   ↓
3. Automáticamente elimina todos los order_items de esos productos
```

#### 3. Response
```http
HTTP/1.1 204 No Content
```

### Ejemplo 4: Obtener Pedidos de un Cliente con Items

#### 1. Request
```http
GET /api/orders/customer/1/with-items
```

#### 2. El servicio usa JOIN FETCH para evitar N+1
```java
@Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.customer.id = :customerId")
```

#### 3. Response (Optimizada)
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-2024-001",
    "totalAmount": 1229.97,
    "status": "DELIVERED",
    "orderItems": [
      {
        "id": 1,
        "quantity": 1,
        "unitPrice": 899.99,
        "subtotal": 899.99,
        "product": { "name": "Laptop HP Pavilion" }
      }
    ]
  }
]
```

---

## 📊 Resumen de la Arquitectura

### Entidades: 5
- Category
- Product
- Customer
- Order
- OrderItem

### Servicios: 4
- CategoryService
- ProductService
- CustomerService
- OrderService

### Repositorios: 5
- CategoryRepository
- ProductRepository
- CustomerRepository
- OrderRepository
- OrderItemRepository

### Controladores: 4
- CategoryController
- ProductController
- CustomerController
- OrderController

### Total de Endpoints: 50+

---

## ✅ Verificación de Requisitos

| Requisito | Estado | Implementación |
|-----------|--------|----------------|
| ✅ Proyecto Spring Boot | Completado | `pom.xml` con spring-boot-starter |
| ✅ Librería JPA | Completado | spring-boot-starter-data-jpa |
| ✅ Conexión a BD | Completado | H2 (dev) / MySQL (prod) |
| ✅ Arquitectura 3 capas | Completado | Controller → Service → Repository |
| ✅ Todas las entidades | Completado | 5 entidades con 3 capas cada una |
| ✅ Modificaciones en cascada | Completado | `cascade = CascadeType.ALL` |
| ✅ Scripts de inicialización | Completado | schema.sql + data.sql |
| ✅ Documentación | Completado | JavaDoc completo + guías |

---

**¡Arquitectura de 3 capas completada e implementada para todas las entidades!** 🎉

