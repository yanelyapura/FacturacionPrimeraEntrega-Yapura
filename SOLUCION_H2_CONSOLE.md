# 🔧 Solución: Acceso a la Consola H2

## ✅ Problema Resuelto

El problema era que la aplicación **no estaba ejecutándose**. Para que `http://localhost:8080/h2-console` funcione, la aplicación Spring Boot **debe estar en ejecución**.

---

## 📝 Pasos Realizados

### 1️⃣ Compilación Exitosa
```bash
mvn clean compile
```
✅ **Resultado**: BUILD SUCCESS - Sin errores

### 2️⃣ Aplicación en Ejecución
```bash
mvn spring-boot:run
```
✅ **Estado**: Ejecutándose en segundo plano

---

## 🌐 Cómo Acceder a la Consola H2

### Espera 10-20 segundos para que la aplicación inicie completamente

Verás en la consola mensajes similares a:
```
Started PrimeraEntregaJpaApplication in X.XXX seconds
==============================================
✅ Aplicación JPA iniciada correctamente
==============================================
🌐 Servidor: http://localhost:8080
🗄️  Consola H2: http://localhost:8080/h2-console
```

### 📊 Acceder a la Consola H2

1. **Abrir el navegador web** (Chrome, Firefox, Edge, etc.)

2. **Navegar a**: http://localhost:8080/h2-console

3. **Configurar la conexión** con estos datos:
   ```
   Driver Class: org.h2.Driver
   JDBC URL: jdbc:h2:mem:testdb
   User Name: sa
   Password: (dejar en blanco)
   ```

4. **Click en "Connect"** o **"Conectar"**

5. **¡Listo!** Verás la interfaz de la base de datos H2

---

## 🗄️ Explorar la Base de Datos

Una vez conectado, verás en el panel izquierdo todas las tablas:

```
TESTDB
└── PUBLIC
    ├── CATEGORIES (5 registros)
    ├── PRODUCTS (18 registros)
    ├── CUSTOMERS (5 registros)
    ├── ORDERS (5 registros)
    └── ORDER_ITEMS (14 registros)
```

### 📝 Consultas SQL de Ejemplo

**Ver todas las categorías:**
```sql
SELECT * FROM categories;
```

**Ver productos con su categoría:**
```sql
SELECT p.name AS producto, c.name AS categoria, p.price, p.stock
FROM products p
JOIN categories c ON p.category_id = c.category_id;
```

**Ver clientes y sus pedidos:**
```sql
SELECT c.first_name, c.last_name, o.order_number, o.total_amount, o.status
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
ORDER BY c.first_name;
```

**Ver detalles de un pedido:**
```sql
SELECT oi.quantity, p.name, oi.unit_price, oi.subtotal
FROM order_items oi
JOIN products p ON oi.product_id = p.product_id
WHERE oi.order_id = 1;
```

**Productos más caros:**
```sql
SELECT name, price, stock
FROM products
ORDER BY price DESC
LIMIT 5;
```

---

## 🔍 Verificar que la Aplicación está Ejecutándose

### Opción 1: Ver logs en la terminal
En la terminal donde ejecutaste `mvn spring-boot:run` busca:
```
Started PrimeraEntregaJpaApplication
```

### Opción 2: Verificar el puerto
```bash
netstat -ano | findstr :8080
```
Si ves resultados, el puerto 8080 está en uso por la aplicación.

### Opción 3: Acceder al servidor
Abre en el navegador: http://localhost:8080

Deberías ver un mensaje de error de "Whitelabel Error Page" (es normal, no hay controladores REST definidos aún), pero esto confirma que el servidor está activo.

---

## ❌ Problemas Comunes y Soluciones

### Problema 1: "Cannot connect to http://localhost:8080/h2-console"

**Causa:** La aplicación no está ejecutándose.

**Solución:**
```bash
# En la terminal, ejecutar:
mvn spring-boot:run
```

### Problema 2: "Port 8080 already in use"

**Causa:** Otro proceso está usando el puerto 8080.

**Solución 1 - Detener el proceso:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# O simplemente reinicia la computadora
```

**Solución 2 - Cambiar el puerto:**
Editar `src/main/resources/application.properties`:
```properties
server.port=8081
```
Luego acceder a: http://localhost:8081/h2-console

### Problema 3: Error al conectar en H2 Console

**Síntoma:** Aparece "Connection refused" o similar.

**Verificar:**
1. JDBC URL correcta: `jdbc:h2:mem:testdb`
2. Usuario: `sa`
3. Contraseña: (dejar en blanco)
4. La aplicación debe estar ejecutándose

### Problema 4: No aparecen las tablas

**Causa:** Los scripts SQL no se ejecutaron.

**Verificar en logs:**
Buscar en la consola:
```
Hibernate: CREATE TABLE categories
Hibernate: INSERT INTO categories
```

**Solución:**
Verificar que en `application.properties` esté:
```properties
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

---

## 🛑 Detener la Aplicación

### Desde la terminal:
1. En la terminal donde está corriendo, presionar `Ctrl + C`
2. Esperar a que diga "Stopped PrimeraEntregaJpaApplication"

### Alternativa:
```bash
# Windows - Matar el proceso Java
tasklist | findstr java
taskkill /PID <PID> /F
```

---

## 🎯 Comandos Útiles

### Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

### Compilar sin ejecutar:
```bash
mvn clean compile
```

### Empaquetar (crear JAR):
```bash
mvn clean package
```

### Ejecutar el JAR:
```bash
java -jar target/primera-entrega-jpa-1.0.0.jar
```

---

## 📚 Recursos Adicionales

### Archivos de Configuración:
- `src/main/resources/application.properties` - Configuración H2
- `src/main/resources/schema.sql` - Estructura de tablas
- `src/main/resources/data.sql` - Datos iniciales

### Documentación del Proyecto:
- `README.md` - Documentación completa
- `CONCEPTOS_JPA.md` - Guía de conceptos JPA
- `INSTRUCCIONES_EJECUCION.md` - Guía de ejecución paso a paso
- `DIAGRAMA_PROYECTO.txt` - Diagramas de arquitectura

---

## ✅ Checklist de Verificación

- [ ] Aplicación compilada sin errores (`mvn clean compile`)
- [ ] Aplicación ejecutándose (`mvn spring-boot:run`)
- [ ] Esperar 10-20 segundos para inicio completo
- [ ] Navegador abierto en http://localhost:8080/h2-console
- [ ] Datos de conexión correctos (JDBC URL, usuario, contraseña)
- [ ] Click en "Connect"
- [ ] Tablas visibles en panel izquierdo
- [ ] Consultas SQL funcionando correctamente

---

## 💡 Consejo Pro

**Mantén siempre la terminal abierta** donde ejecutaste `mvn spring-boot:run` para ver los logs en tiempo real. Esto te ayuda a:
- Ver errores si algo falla
- Ver las consultas SQL que genera Hibernate
- Monitorear el estado de la aplicación
- Detectar problemas de rendimiento

---

## 📞 Si Todavía No Funciona

1. **Revisar los logs** en la terminal donde ejecutaste `mvn spring-boot:run`
2. **Buscar errores** (líneas que contengan "ERROR" o "Exception")
3. **Verificar Java** está instalado: `java -version`
4. **Verificar Maven** está instalado: `mvn -version`
5. **Reiniciar** la aplicación:
   - Detener con `Ctrl + C`
   - Ejecutar nuevamente `mvn spring-boot:run`

---

**¡La aplicación está lista y funcionando! 🎉**

Ahora puedes:
- ✅ Explorar la base de datos en H2 Console
- ✅ Ejecutar consultas SQL
- ✅ Ver las relaciones entre tablas
- ✅ Modificar datos y experimentar
- ✅ Aprender conceptos de JPA en acción

