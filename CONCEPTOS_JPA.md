# 📚 Guía Completa de Conceptos JPA

## 📖 Índice
1. [¿Qué es JPA?](#qué-es-jpa)
2. [Frameworks que implementan JPA](#frameworks-que-implementan-jpa)
3. [Entidades (Entity)](#entidades-entity)
4. [Campos y Propiedades Persistentes](#campos-y-propiedades-persistentes)
5. [Claves Primarias](#claves-primarias)
6. [Relaciones entre Entidades](#relaciones-entre-entidades)
7. [EntityManager](#entitymanager)
8. [Inicialización de Base de Datos](#inicialización-de-base-de-datos)

---

## 🎯 ¿Qué es JPA?

**JPA (Java Persistence API)** es una especificación de Java que permite mapear objetos Java a tablas de base de datos relacionales. Implementa el concepto de **ORM (Object-Relational Mapping)**.

### Beneficios de JPA:
✅ **Abstracción de la BD**: No necesitas escribir SQL directamente  
✅ **Portabilidad**: Cambia de BD sin cambiar código  
✅ **Productividad**: Menos código boilerplate  
✅ **Mantenibilidad**: Código más limpio y orientado a objetos  
✅ **Type Safety**: Validación en tiempo de compilación  

### Ejemplo de Mapeo:

**Clase Java (Entidad):**
```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "price")
    private BigDecimal price;
}
```

**Tabla SQL equivalente:**
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price DECIMAL(10, 2)
);
```

---

## 🛠️ Frameworks que implementan JPA

JPA es solo una **especificación** (interfaz), no una implementación. Los frameworks más populares que la implementan son:

### 1. **Hibernate** 🔥 (Más popular)
- Implementación de referencia más usada
- Características avanzadas (caché de segundo nivel, lazy loading, etc.)
- Módulos adicionales: Hibernate Validator, Hibernate Search

### 2. **EclipseLink**
- Implementación de referencia oficial de JPA
- Usado en GlassFish y otros servidores

### 3. **OpenJPA**
- Implementación de Apache
- Enfoque en entornos empresariales

### En este proyecto usamos:
- **Hibernate** como implementación de JPA
- **Spring Data JPA** como capa de abstracción adicional

---

## 📦 Entidades (Entity)

Una **entidad** es una clase Java que se mapea a una tabla de base de datos.

### Requisitos de una Entidad:

1. ✅ Debe tener la anotación `@Entity`
2. ✅ Debe tener un constructor sin argumentos (público o protegido)
3. ✅ No puede ser `final`
4. ✅ Sus atributos persistentes deben ser `private` o `protected`
5. ✅ Debe tener una clave primaria anotada con `@Id`

### Ejemplo de Entidad:

```java
@Entity                                    // 1. Marca la clase como entidad
@Table(name = "customers")                 // Opcional: especifica nombre de tabla
public class Customer implements Serializable {  // Serializable recomendado
    
    @Id                                    // 2. Define clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;              // 3. Atributos privados
    
    // 4. Constructor sin argumentos (requerido)
    public Customer() {
    }
    
    // Constructor con parámetros (opcional)
    public Customer(String firstName) {
        this.firstName = firstName;
    }
    
    // Getters y setters...
}
```

### Anotaciones Importantes:

| Anotación | Descripción | Ejemplo |
|-----------|-------------|---------|
| `@Entity` | Define una clase como entidad JPA | `@Entity` |
| `@Table` | Especifica el nombre de la tabla | `@Table(name = "products")` |
| `@Id` | Define la clave primaria | `@Id` |
| `@Column` | Configura una columna | `@Column(name = "email", unique = true)` |
| `@Transient` | Campo no persistente | `@Transient` |

---

## 🔑 Campos y Propiedades Persistentes

Los **campos persistentes** son aquellos que se guardan en la base de datos.

### Dos formas de definir persistencia:

#### 1. **Acceso por Campo (Field Access)** - Más común
Anotaciones directamente en los atributos:

```java
@Entity
public class Product {
    @Id
    private Long id;  // Anotación en el campo
    
    @Column(name = "product_name")
    private String name;
}
```

#### 2. **Acceso por Propiedad (Property Access)**
Anotaciones en los getters:

```java
@Entity
public class Product {
    private Long id;
    private String name;
    
    @Id  // Anotación en el getter
    public Long getId() {
        return id;
    }
    
    @Column(name = "product_name")
    public String getName() {
        return name;
    }
}
```

### Campos NO Persistentes:

Usa `@Transient` para excluir campos:

```java
@Entity
public class Product {
    @Id
    private Long id;
    
    private String name;
    
    @Transient  // Este campo NO se guarda en la BD
    private String temporaryData;
}
```

### Colecciones Permitidas:

JPA soporta estas colecciones para relaciones:

```java
// Collection (interface genérica)
@OneToMany
private Collection<OrderItem> items;

// Set (no permite duplicados)
@OneToMany
private Set<OrderItem> items;

// List (permite duplicados, mantiene orden)
@OneToMany
private List<OrderItem> items;

// Map (pares clave-valor)
@OneToMany
@MapKey(name = "id")
private Map<Long, OrderItem> items;
```

---

## 🔐 Claves Primarias (Primary Key)

Cada entidad **DEBE** tener un identificador único (clave primaria).

### Estrategias de Generación:

#### 1. **AUTO** (Por defecto)
JPA elige la estrategia automáticamente:

```java
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
```

#### 2. **IDENTITY** (Más común)
La BD genera el ID (AUTO_INCREMENT en MySQL):

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

#### 3. **SEQUENCE**
Usa secuencias de la BD (Oracle, PostgreSQL):

```java
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
@SequenceGenerator(name = "product_seq", sequenceName = "product_sequence")
private Long id;
```

#### 4. **TABLE**
Usa una tabla especial para generar IDs:

```java
@Id
@GeneratedValue(strategy = GenerationType.TABLE)
private Long id;
```

### Claves Primarias Compuestas:

Cuando la clave primaria tiene múltiples columnas:

```java
@Embeddable
public class OrderItemId implements Serializable {
    private Long orderId;
    private Long productId;
    
    // Constructor, equals, hashCode...
}

@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    
    // Otros campos...
}
```

### Requisitos de Claves Primarias:

✅ Debe implementar `hashCode()` y `equals()`  
✅ Debe ser `Serializable` (para claves compuestas)  
✅ No puede ser `null`  
✅ Debe ser inmutable (no cambiar después de crear la entidad)  

---

## 🔗 Relaciones entre Entidades

Las relaciones definen cómo se conectan las entidades entre sí.

### 1. **@OneToOne** (Uno a Uno)

Una entidad se relaciona con exactamente una instancia de otra.

**Ejemplo:** Usuario ↔ Perfil

```java
@Entity
public class User {
    @Id
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}

@Entity
public class Profile {
    @Id
    private Long id;
    
    @OneToOne(mappedBy = "profile")
    private User user;
}
```

### 2. **@OneToMany** y **@ManyToOne** (Uno a Muchos)

Una entidad se relaciona con múltiples instancias de otra.

**Ejemplo:** Categoría ↔ Productos (una categoría tiene muchos productos)

```java
// Lado "One" - Category
@Entity
public class Category {
    @Id
    private Long id;
    
    // mappedBy indica que Product es el dueño de la relación
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}

// Lado "Many" - Product
@Entity
public class Product {
    @Id
    private Long id;
    
    // JoinColumn define la FK en la tabla products
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```

**Tabla SQL resultante:**
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    category_id BIGINT,  -- Clave foránea
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### 3. **@ManyToMany** (Muchos a Muchos)

Múltiples instancias de ambas entidades se relacionan entre sí.

**Ejemplo:** Estudiantes ↔ Cursos (un estudiante tiene muchos cursos, un curso tiene muchos estudiantes)

```java
@Entity
public class Student {
    @Id
    private Long id;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "student_course",  // Tabla intermedia
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id
    private Long id;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

**Tabla SQL intermedia:**
```sql
CREATE TABLE student_course (
    student_id BIGINT,
    course_id BIGINT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);
```

### Parámetros Importantes de Relaciones:

#### **cascade** - Propagar operaciones

```java
@OneToMany(cascade = CascadeType.ALL)  // Propaga todas las operaciones
private List<OrderItem> items;

// Tipos de cascade:
CascadeType.PERSIST   // Solo al guardar
CascadeType.MERGE     // Solo al actualizar
CascadeType.REMOVE    // Solo al eliminar
CascadeType.REFRESH   // Solo al refrescar
CascadeType.DETACH    // Solo al desasociar
CascadeType.ALL       // Todas las anteriores
```

#### **fetch** - Estrategia de carga

```java
// LAZY: Carga bajo demanda (recomendado para *ToMany)
@OneToMany(fetch = FetchType.LAZY)
private List<OrderItem> items;

// EAGER: Carga inmediata (por defecto en *ToOne)
@ManyToOne(fetch = FetchType.EAGER)
private Category category;
```

#### **orphanRemoval** - Eliminar huérfanos

```java
// Si un item se remueve de la lista, se elimina de la BD
@OneToMany(orphanRemoval = true)
private List<OrderItem> items;
```

#### **mappedBy** - Lado no propietario

```java
// Indica qué campo en la otra entidad es el dueño
@OneToMany(mappedBy = "category")
private List<Product> products;
```

### Sincronización Bidireccional:

Es importante mantener ambos lados sincronizados:

```java
@Entity
public class Category {
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
    
    // Método helper para mantener sincronización
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);  // Sincroniza el otro lado
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }
}
```

---

## 🎮 EntityManager

El **EntityManager** es la interfaz principal para interactuar con el contexto de persistencia.

### Operaciones Principales:

```java
// 1. PERSIST - Guardar nueva entidad
Product product = new Product("Laptop", 999.99);
entityManager.persist(product);  // INSERT INTO products...

// 2. FIND - Buscar por ID
Product found = entityManager.find(Product.class, 1L);  // SELECT * FROM products WHERE id = 1

// 3. MERGE - Actualizar entidad
product.setPrice(899.99);
entityManager.merge(product);  // UPDATE products SET price = 899.99...

// 4. REMOVE - Eliminar entidad
entityManager.remove(product);  // DELETE FROM products WHERE id = 1

// 5. FLUSH - Sincronizar con la BD
entityManager.flush();  // Ejecuta todas las operaciones pendientes

// 6. DETACH - Desasociar entidad del contexto
entityManager.detach(product);  // La entidad ya no es gestionada

// 7. REFRESH - Recargar desde la BD
entityManager.refresh(product);  // SELECT * FROM products WHERE id = 1
```

### Estados de una Entidad:

```
┌──────────────────────────────────────────────────────┐
│                                                      │
│  NEW (Transient)                                     │
│  └─> persist() ──> MANAGED (Persistent)             │
│                    └─> flush() ──> SYNCHRONIZED      │
│                    └─> detach() ──> DETACHED         │
│                    └─> remove() ──> REMOVED          │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### Spring Data JPA (Abstracción):

Con **Spring Data JPA**, no necesitas usar EntityManager directamente:

```java
// En lugar de esto (EntityManager):
entityManager.persist(product);
Product found = entityManager.find(Product.class, 1L);

// Haces esto (Repository):
productRepository.save(product);
Product found = productRepository.findById(1L).orElse(null);
```

---

## 🚀 Inicialización de Base de Datos en Spring Boot

Spring Boot facilita la inicialización automática de la base de datos.

### Dos formas principales:

#### 1. **Hibernate Auto DDL** (desarrollo)

```properties
# application.properties
spring.jpa.hibernate.ddl-auto=create       # Crea tablas al iniciar (DESTRUYE datos)
spring.jpa.hibernate.ddl-auto=create-drop  # Crea al iniciar, elimina al cerrar
spring.jpa.hibernate.ddl-auto=update       # Actualiza esquema (mantiene datos)
spring.jpa.hibernate.ddl-auto=validate     # Solo valida esquema
spring.jpa.hibernate.ddl-auto=none         # No hace nada (recomendado para producción)
```

⚠️ **Advertencia:** `update` puede causar problemas. No usar en producción.

#### 2. **Scripts SQL** (recomendado)

```properties
# application.properties
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

**schema.sql** - Define estructura:
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);
```

**data.sql** - Carga datos iniciales:
```sql
INSERT INTO products (name, price) VALUES
('Laptop', 899.99),
('Mouse', 29.99);
```

### Orden de Ejecución:

```
1. Spring Boot inicia
2. Se ejecuta schema.sql (crea tablas)
3. Se ejecuta data.sql (inserta datos)
4. EntityManager está listo para usar
```

---

## 📋 Resumen Final

### Conceptos Clave:

| Concepto | Descripción |
|----------|-------------|
| **JPA** | Especificación para mapear objetos a BD |
| **Hibernate** | Implementación más popular de JPA |
| **Entidad** | Clase Java que se mapea a una tabla |
| **@Id** | Define la clave primaria |
| **@OneToMany** | Relación uno a muchos |
| **@ManyToOne** | Relación muchos a uno |
| **@ManyToMany** | Relación muchos a muchos |
| **EntityManager** | Interfaz para operaciones CRUD |
| **Repository** | Abstracción de Spring Data JPA |
| **schema.sql** | Script para crear estructura de BD |
| **data.sql** | Script para cargar datos iniciales |

### Mejores Prácticas:

✅ Usa `@GeneratedValue` para IDs  
✅ Implementa `hashCode()` y `equals()` usando ID  
✅ Usa `fetch = LAZY` para relaciones *ToMany  
✅ Mantén sincronización bidireccional  
✅ Usa `orphanRemoval` para limpiar huérfanos  
✅ Prefiere scripts SQL sobre `ddl-auto` en producción  
✅ Documenta tus entidades y relaciones  
✅ Valida datos con Bean Validation  

---

**Este documento complementa el proyecto de ejemplo con explicaciones detalladas de todos los conceptos JPA** 🎓

