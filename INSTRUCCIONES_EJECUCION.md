# 🚀 Instrucciones de Ejecución del Proyecto

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

### ✅ Java Development Kit (JDK) 17 o superior
```bash
# Verificar instalación de Java
java -version

# Debe mostrar algo como:
# java version "17.0.x" o superior
```

**Descargar Java:**
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [OpenJDK](https://adoptium.net/)

### ✅ Apache Maven 3.6+
```bash
# Verificar instalación de Maven
mvn -version

# Debe mostrar algo como:
# Apache Maven 3.x.x
```

**Descargar Maven:**
- [Apache Maven](https://maven.apache.org/download.cgi)

### ✅ IDE (Opcional pero recomendado)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community o Ultimate)
- [Eclipse IDE](https://www.eclipse.org/downloads/)
- [Visual Studio Code](https://code.visualstudio.com/) con extensión Java

---

## 🔧 Instalación y Configuración

### Paso 1: Navegar al directorio del proyecto

```bash
cd "C:\Users\Yanu\Documents\primera entrega java"
```

### Paso 2: Limpiar y compilar el proyecto

```bash
# Limpiar compilaciones anteriores y compilar
mvn clean install

# Si quieres omitir los tests (más rápido):
mvn clean install -DskipTests
```

**Salida esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX s
```

---

## ▶️ Ejecutar la Aplicación

### Opción 1: Usando Maven (Línea de comandos)

```bash
mvn spring-boot:run
```

### Opción 2: Usando el JAR compilado

```bash
# Primero compilar
mvn clean package

# Luego ejecutar
java -jar target/primera-entrega-jpa-1.0.0.jar
```

### Opción 3: Desde tu IDE

#### IntelliJ IDEA:
1. Abrir el proyecto (`File > Open` y seleccionar la carpeta)
2. Esperar a que Maven descargue las dependencias
3. Buscar la clase `PrimeraEntregaJpaApplication.java`
4. Click derecho > `Run 'PrimeraEntregaJpaApplication'`

#### Eclipse:
1. `File > Import > Maven > Existing Maven Projects`
2. Seleccionar la carpeta del proyecto
3. Click derecho en el proyecto > `Run As > Spring Boot App`

#### Visual Studio Code:
1. Abrir la carpeta del proyecto
2. Instalar la extensión "Spring Boot Extension Pack"
3. Presionar `F5` o usar el botón "Run" en la clase principal

---

## 🌐 Acceder a la Aplicación

Una vez que la aplicación esté ejecutándose, verás en la consola:

```
==============================================
✅ Aplicación JPA iniciada correctamente
==============================================
🌐 Servidor: http://localhost:8080
🗄️  Consola H2: http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:testdb
   Usuario: sa
   Contraseña: (dejar en blanco)
==============================================
```

### 📊 Acceder a la Consola H2

1. **Abrir el navegador** y navegar a: http://localhost:8080/h2-console

2. **Configurar la conexión:**
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **User Name:** `sa`
   - **Password:** (dejar en blanco)

3. **Click en "Connect"**

4. **Explorar la base de datos:**
   - En el panel izquierdo verás todas las tablas
   - Puedes ejecutar consultas SQL directamente

### 📝 Consultas SQL de Ejemplo

Prueba estas consultas en la consola H2:

```sql
-- Ver todas las categorías
SELECT * FROM categories;

-- Ver todos los productos con su categoría
SELECT p.name AS producto, c.name AS categoria, p.price, p.stock
FROM products p
JOIN categories c ON p.category_id = c.category_id;

-- Ver todos los clientes activos
SELECT * FROM customers WHERE status = 'ACTIVE';

-- Ver pedidos con información del cliente
SELECT o.order_number, c.first_name, c.last_name, o.status, o.total_amount
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id;

-- Ver items de un pedido específico
SELECT oi.quantity, p.name, oi.unit_price, oi.subtotal
FROM order_items oi
JOIN products p ON oi.product_id = p.product_id
WHERE oi.order_id = 1;

-- Productos más vendidos
SELECT p.name, SUM(oi.quantity) AS total_vendido
FROM products p
JOIN order_items oi ON p.product_id = oi.product_id
GROUP BY p.name
ORDER BY total_vendido DESC;
```

---

## 🛠️ Configuración Alternativa: MySQL

Si quieres usar MySQL en lugar de H2:

### Paso 1: Crear la base de datos

```sql
CREATE DATABASE proyecto_jpa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Paso 2: Configurar application-mysql.properties

Editar el archivo `src/main/resources/application-mysql.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_jpa
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

### Paso 3: Ejecutar con perfil MySQL

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## 🐛 Solución de Problemas

### Error: "java: error: invalid target release: 17"

**Problema:** La versión de Java no es compatible.

**Solución:**
1. Verificar que tienes Java 17+: `java -version`
2. Si tienes una versión anterior, descarga Java 17
3. Configurar la variable de entorno `JAVA_HOME`

### Error: "mvn: command not found"

**Problema:** Maven no está instalado o no está en el PATH.

**Solución:**
1. Descargar e instalar Maven desde https://maven.apache.org/download.cgi
2. Agregar Maven al PATH del sistema
3. Reiniciar la terminal

### Error: "Port 8080 already in use"

**Problema:** Otro proceso está usando el puerto 8080.

**Solución 1 - Cambiar el puerto:**
Editar `application.properties`:
```properties
server.port=8081
```

**Solución 2 - Detener el proceso:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Error: "Could not resolve dependencies"

**Problema:** Maven no puede descargar las dependencias.

**Solución:**
```bash
# Limpiar caché de Maven
mvn clean

# Forzar actualización de dependencias
mvn clean install -U
```

### La consola H2 no carga

**Problema:** La consola H2 está deshabilitada.

**Solución:**
Verificar que en `application.properties` esté:
```properties
spring.h2.console.enabled=true
```

---

## 📊 Verificar que todo funciona

### 1. Verificar que la aplicación inició correctamente

Buscar en los logs:
```
Started PrimeraEntregaJpaApplication in X.XXX seconds
```

### 2. Verificar que los datos se cargaron

```sql
-- En la consola H2, ejecutar:
SELECT COUNT(*) FROM products;     -- Debe retornar 18
SELECT COUNT(*) FROM customers;    -- Debe retornar 5
SELECT COUNT(*) FROM orders;       -- Debe retornar 5
SELECT COUNT(*) FROM categories;   -- Debe retornar 5
```

### 3. Verificar los logs de Hibernate

Buscar en la consola las consultas SQL ejecutadas:
```sql
Hibernate: CREATE TABLE categories ...
Hibernate: INSERT INTO categories ...
```

---

## 📚 Recursos Adicionales

### Documentación
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)

### Archivos del Proyecto
- `README.md` - Documentación general del proyecto
- `CONCEPTOS_JPA.md` - Guía completa de conceptos JPA
- `pom.xml` - Configuración de dependencias Maven

### Estructura del Código
```
src/main/java/com/proyecto/jpa/
├── entity/          → Entidades JPA (modelos de datos)
├── repository/      → Interfaces para acceso a datos
└── PrimeraEntregaJpaApplication.java → Clase principal
```

---

## ✅ Checklist de Ejecución Exitosa

- [ ] Java 17+ instalado y verificado
- [ ] Maven instalado y verificado
- [ ] Proyecto compilado sin errores (`mvn clean install`)
- [ ] Aplicación ejecutándose (`mvn spring-boot:run`)
- [ ] Consola H2 accesible (http://localhost:8080/h2-console)
- [ ] Datos cargados correctamente (verificar con consultas SQL)
- [ ] No hay errores en los logs

---

## 🎓 Siguiente Paso: Explorar el Código

Una vez que la aplicación esté funcionando:

1. **Explorar las entidades** en `src/main/java/com/proyecto/jpa/entity/`
2. **Revisar los repositorios** en `src/main/java/com/proyecto/jpa/repository/`
3. **Leer la documentación** en `README.md` y `CONCEPTOS_JPA.md`
4. **Experimentar con consultas SQL** en la consola H2
5. **Modificar los datos** en `data.sql` y reiniciar la aplicación

---

## 💡 Consejos

✅ **Mantén los logs abiertos** para ver las consultas SQL que genera Hibernate  
✅ **Usa la consola H2** para verificar los datos y ejecutar consultas  
✅ **Lee los comentarios** en el código para entender cada concepto  
✅ **Experimenta** modificando entidades y relaciones  
✅ **Consulta la documentación** cuando tengas dudas  

---

**¡Listo! Ahora tienes todo lo necesario para ejecutar y explorar el proyecto JPA** 🎉

