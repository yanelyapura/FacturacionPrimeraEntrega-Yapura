-- =====================================================
-- Script para crear la estructura de la base de datos
-- Schema SQL para proyecto JPA
-- =====================================================

-- Eliminar tablas si existen (para desarrollo)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS customers;

-- =====================================================
-- Tabla: categories
-- Almacena las categorías de productos
-- =====================================================
CREATE TABLE categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    CONSTRAINT uk_category_name UNIQUE (name)
);

-- =====================================================
-- Tabla: products
-- Almacena los productos del catálogo
-- =====================================================
CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES categories(category_id) ON DELETE CASCADE,
    CONSTRAINT chk_product_price CHECK (price >= 0),
    CONSTRAINT chk_product_stock CHECK (stock >= 0)
);

-- Índice para mejorar búsquedas por categoría
CREATE INDEX idx_product_category ON products(category_id);
CREATE INDEX idx_product_name ON products(name);

-- =====================================================
-- Tabla: customers
-- Almacena información de los clientes
-- =====================================================
CREATE TABLE customers (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(500),
    birth_date DATE,
    registration_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT uk_customer_email UNIQUE (email),
    CONSTRAINT chk_customer_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);

-- Índice para búsquedas por email
CREATE INDEX idx_customer_email ON customers(email);

-- =====================================================
-- Tabla: orders
-- Almacena los pedidos realizados por clientes
-- =====================================================
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    shipping_address VARCHAR(500),
    notes TEXT,
    customer_id BIGINT NOT NULL,
    CONSTRAINT uk_order_number UNIQUE (order_number),
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) 
        REFERENCES customers(customer_id) ON DELETE CASCADE,
    CONSTRAINT chk_order_status CHECK (status IN ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
    CONSTRAINT chk_order_total CHECK (total_amount >= 0)
);

-- Índices para mejorar rendimiento en consultas
CREATE INDEX idx_order_customer ON orders(customer_id);
CREATE INDEX idx_order_date ON orders(order_date);
CREATE INDEX idx_order_status ON orders(status);

-- =====================================================
-- Tabla: order_items
-- Almacena los items de cada pedido (tabla intermedia)
-- =====================================================
CREATE TABLE order_items (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) 
        REFERENCES products(product_id) ON DELETE RESTRICT,
    CONSTRAINT chk_orderitem_quantity CHECK (quantity > 0),
    CONSTRAINT chk_orderitem_price CHECK (unit_price >= 0),
    CONSTRAINT chk_orderitem_subtotal CHECK (subtotal >= 0)
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_orderitem_order ON order_items(order_id);
CREATE INDEX idx_orderitem_product ON order_items(product_id);

-- =====================================================
-- Comentarios sobre el diseño
-- =====================================================
-- 1. Todas las tablas usan claves primarias auto-incrementales (BIGINT)
-- 2. Se usan constraints para garantizar integridad referencial
-- 3. Se añaden checks para validar datos (precios positivos, estados válidos, etc.)
-- 4. Los índices mejoran el rendimiento de consultas frecuentes
-- 5. ON DELETE CASCADE: elimina registros relacionados automáticamente
-- 6. ON DELETE RESTRICT: previene eliminación si hay registros relacionados

