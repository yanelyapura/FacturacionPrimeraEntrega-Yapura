

# 📦 Estructura de Paquetes del Proyecto JPA

## 📋 Índice
1. [Visión General](#visión-general)
2. [Estructura Completa](#estructura-completa)
3. [Descripción de Paquetes](#descripción-de-paquetes)
4. [Patrones de Diseño](#patrones-de-diseño)
5. [Arquitectura en Capas](#arquitectura-en-capas)
6. [Convenciones de Nomenclatura](#convenciones-de-nomenclatura)

---

## 🎯 Visión General

Este proyecto sigue una **arquitectura en capas** bien organizada, con paquetes claramente definidos que separan responsabilidades según su función. Esta estructura facilita el mantenimiento, la escalabilidad y la comprensión del código.

### Principios Aplicados

✅ **Separation of Concerns** - Cada paquete tiene una responsabilidad específica  
✅ **Single Responsibility Principle** - Una clase, una responsabilidad  
✅ **Dependency Injection** - Las dependencias se inyectan, no se crean  
✅ **Don't Repeat Yourself (DRY)** - Código reutilizable  
✅ **Clean Architecture** - Capas bien definidas  

---

## 📁 Estructura Completa

```
src/main/java/com/proyecto/jpa/
│
├── 📦 entity/                          # Capa de Modelo (Entidades JPA)
│   ├── Category.java                   # Entidad Categoría
│   ├── Product.java                    # Entidad Producto
│   ├── Customer.java                   # Entidad Cliente
│   ├── Order.java                      # Entidad Pedido
│   ├── OrderItem.java                  # Entidad Item de Pedido
│   └── package-info.java               # Documentación del paquete
│
├── 📦 repository/                      # Capa de Acceso a Datos
│   ├── CategoryRepository.java         # Repositorio de Categorías
│   ├── ProductRepository.java          # Repositorio de Productos
│   ├── CustomerRepository.java         # Repositorio de Clientes
│   ├── OrderRepository.java            # Repositorio de Pedidos
│   ├── OrderItemRepository.java        # Repositorio de Items
│   └── package-info.java               # Documentación del paquete
│
├── 📦 service/                         # Capa de Lógica de Negocio
│   ├── OrderService.java               # Servicio de Pedidos
│   └── package-info.java               # Documentación del paquete
│
├── 📦 config/                          # Capa de Configuración
│   ├── DatabaseConfig.java             # Configuración de BD y JPA
│   └── package-info.java               # Documentación del paquete
│
├── PrimeraEntregaJpaApplication.java   # Clase Principal
└── package-info.java                   # Documentación del paquete raíz
```

---

## 📖 Descripción de Paquetes

### 1. 📦 `entity` - Capa de Modelo

**Propósito:** Contiene las entidades JPA que representan el modelo de dominio.

**Responsabilidades:**
- Mapear objetos Java a tablas de base de datos
- Definir relaciones entre entidades
- Implementar validaciones de datos
- Definir el comportamiento del dominio

**Características:**
- Anotaciones JPA (`@Entity`, `@Table`, `@Column`)
- Relaciones bidireccionales
- Validaciones con Bean Validation
- Métodos helper para mantener sincronización

**Ejemplo:**
```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    // ... más campos y métodos
}
```

---

### 2. 📦 `repository` - Capa de Acceso a Datos

**Propósito:** Abstrae el acceso a la base de datos mediante el patrón Repository.

**Responsabilidades:**
- Proporcionar operaciones CRUD
- Definir consultas personalizadas
- Abstraer la lógica de persistencia
- Facilitar el testing con mocks

**Características:**
- Extienden `JpaRepository`
- Query Methods (métodos por convención)
- Consultas JPQL con `@Query`
- Consultas SQL nativas

**Ejemplo:**
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.price < :maxPrice")
    List<Product> findCheapProducts(@Param("maxPrice") BigDecimal maxPrice);
}
```

---

### 3. 📦 `service` - Capa de Lógica de Negocio

**Propósito:** Implementa la lógica de negocio y coordina operaciones complejas.

**Responsabilidades:**
- Aplicar reglas de negocio
- Coordinar múltiples repositorios
- Gestionar transacciones
- Validar operaciones
- Transformar datos

**Características:**
- Anotación `@Service`
- Métodos transaccionales con `@Transactional`
- Lógica de negocio compleja
- Coordinación entre entidades

**Ejemplo:**
```java
@Service
@Transactional(readOnly = true)
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        // Validaciones
        // Lógica de negocio
        // Coordinación entre repositorios
        return orderRepository.save(order);
    }
}
```

---

### 4. 📦 `config` - Capa de Configuración

**Propósito:** Configura componentes de Spring Boot y aspectos del sistema.

**Responsabilidades:**
- Definir beans de Spring
- Configurar JPA y repositorios
- Habilitar características (transacciones, etc.)
- Centralizar configuraciones

**Características:**
- Anotación `@Configuration`
- Definición de beans con `@Bean`
- Habilitación de features (`@EnableJpaRepositories`)

**Ejemplo:**
```java
@Configuration
@EnableJpaRepositories(basePackages = "com.proyecto.jpa.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Configuración de beans
}
```

---

## 🏗️ Patrones de Diseño

### 1. **Repository Pattern**
- **Ubicación:** Paquete `repository`
- **Propósito:** Abstraer el acceso a datos
- **Ventaja:** Desacopla la lógica de negocio de la persistencia

### 2. **Service Layer Pattern**
- **Ubicación:** Paquete `service`
- **Propósito:** Encapsular lógica de negocio
- **Ventaja:** Centraliza y reutiliza operaciones complejas

### 3. **Data Transfer Object (DTO)** *(implícito)*
- **Propósito:** Transferir datos entre capas
- **Ventaja:** Evita exponer entidades directamente

### 4. **Dependency Injection**
- **Ubicación:** Todas las capas
- **Propósito:** Inyectar dependencias
- **Ventaja:** Bajo acoplamiento, fácil testing

### 5. **ORM (Object-Relational Mapping)**
- **Ubicación:** Paquete `entity` con JPA/Hibernate
- **Propósito:** Mapear objetos a tablas
- **Ventaja:** Abstrae SQL, trabaja con objetos

---

## 🎯 Arquitectura en Capas

```
┌────────────────────────────────────┐
│   Capa de Presentación (API)      │  ← Futura (Controladores REST)
├────────────────────────────────────┤
│   Capa de Servicio (Service)      │  ← service/
│   • Lógica de Negocio              │
│   • Transacciones                  │
│   • Validaciones                   │
├────────────────────────────────────┤
│   Capa de Acceso a Datos          │  ← repository/
│   (Repository)                     │
│   • Operaciones CRUD               │
│   • Consultas                      │
├────────────────────────────────────┤
│   Capa de Modelo (Entity)         │  ← entity/
│   • Entidades JPA                  │
│   • Relaciones                     │
├────────────────────────────────────┤
│   Base de Datos                    │
│   • Tablas                         │
│   • Constraints                    │
└────────────────────────────────────┘

       Configuración (config/) ────────→ Todas las capas
```

### Flujo de Datos

```
Request → Controller → Service → Repository → Database
                ↓         ↓          ↓
            Validación  Lógica   Persistencia
                         ↓
                    Entity (Model)
```

---

## 📝 Convenciones de Nomenclatura

### Nombres de Clases

| Tipo | Convención | Ejemplo |
|------|-----------|---------|
| **Entidad** | Sustantivo singular | `Product`, `Order`, `Customer` |
| **Repositorio** | `[Entidad]Repository` | `ProductRepository` |
| **Servicio** | `[Dominio]Service` | `OrderService` |
| **Configuración** | `[Aspecto]Config` | `DatabaseConfig` |

### Nombres de Métodos

| Operación | Convención | Ejemplo |
|-----------|-----------|---------|
| **Buscar por ID** | `findById` | `findById(Long id)` |
| **Buscar todos** | `findAll` | `findAll()` |
| **Buscar por campo** | `findBy[Campo]` | `findByName(String name)` |
| **Guardar** | `save` | `save(Product product)` |
| **Eliminar** | `delete` | `deleteById(Long id)` |
| **Contar** | `count` | `countByStatus(Status status)` |
| **Verificar existencia** | `exists` | `existsByEmail(String email)` |

### Nombres de Paquetes

- **Siempre en minúsculas**: `entity`, `repository`, `service`
- **Nombres descriptivos**: Indican claramente su propósito
- **Sin abreviaturas**: `repository` en lugar de `repo`

---

## 🎓 Documentación de Paquetes

Cada paquete incluye un archivo `package-info.java` que documenta:

✅ **Propósito del paquete**  
✅ **Clases principales**  
✅ **Patrones implementados**  
✅ **Ejemplos de uso**  
✅ **Referencias a documentación externa**  

**Ejemplo de package-info.java:**

```java
/**
 * Paquete que contiene las entidades JPA del modelo de dominio.
 * 
 * <h2>Descripción</h2>
 * Este paquete contiene todas las clases de entidad...
 * 
 * @author Proyecto JPA
 * @version 1.0.0
 */
package com.proyecto.jpa.entity;
```

---

## ✅ Ventajas de Esta Estructura

### 1. **Mantenibilidad**
- Código organizado y fácil de encontrar
- Cambios localizados en un solo paquete

### 2. **Escalabilidad**
- Fácil agregar nuevas features
- Estructura clara para nuevos desarrolladores

### 3. **Testabilidad**
- Cada capa se puede testear independientemente
- Fácil crear mocks

### 4. **Reutilización**
- Servicios reutilizables
- Repositorios compartidos

### 5. **Claridad**
- Responsabilidades bien definidas
- Código autodocumentado

---

## 📚 Buenas Prácticas Aplicadas

### ✅ Separación de Responsabilidades
Cada clase tiene una única responsabilidad bien definida.

### ✅ Inyección de Dependencias
Las dependencias se inyectan mediante constructores.

```java
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
```

### ✅ Inmutabilidad cuando es posible
Uso de `final` en campos inyectados.

### ✅ Documentación JavaDoc
Todas las clases y métodos públicos están documentados.

### ✅ Nombres descriptivos
Variables, métodos y clases con nombres claros y significativos.

### ✅ Paquetes cohesivos
Clases relacionadas agrupadas en el mismo paquete.

---

## 🔄 Evolución del Proyecto

### Actual (v1.0.0)
```
✅ entity/        - 5 entidades
✅ repository/    - 5 repositorios
✅ service/       - 1 servicio
✅ config/        - 1 configuración
```

### Futuras Expansiones

Paquetes que se podrían agregar:

```
📦 controller/    - Controladores REST API
📦 dto/          - Data Transfer Objects
📦 exception/    - Excepciones personalizadas
📦 util/         - Utilidades comunes
📦 security/     - Configuración de seguridad
📦 validation/   - Validadores personalizados
```

---

## 📞 Referencias

### Documentación del Proyecto
- `README.md` - Documentación general
- `CONCEPTOS_JPA.md` - Guía de conceptos JPA
- `INSTRUCCIONES_EJECUCION.md` - Guía de ejecución
- `DIAGRAMA_PROYECTO.txt` - Diagramas visuales

### Documentación Externa
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM](https://hibernate.org/orm/)
- [Java Package Guidelines](https://docs.oracle.com/javase/tutorial/java/package/)

---

**Esta estructura de paquetes representa las mejores prácticas de la industria para aplicaciones Java empresariales** 🎓


