-- =====================================================
-- Script para cargar datos iniciales en la base de datos
-- Data SQL para proyecto JPA
-- =====================================================

-- =====================================================
-- Insertar Categorías
-- =====================================================
INSERT INTO categories (name, description) VALUES
('Electrónica', 'Dispositivos electrónicos y accesorios tecnológicos'),
('Ropa', 'Prendas de vestir para todas las edades'),
('Hogar', 'Artículos para el hogar y decoración'),
('Deportes', 'Equipamiento deportivo y fitness'),
('Libros', 'Libros físicos y digitales de diversos géneros');

-- =====================================================
-- Insertar Productos
-- =====================================================
INSERT INTO products (name, description, price, stock, created_at, updated_at, category_id) VALUES
-- Electrónica (category_id = 1)
('Laptop HP Pavilion', 'Laptop con procesador Intel Core i5, 8GB RAM, 512GB SSD', 899.99, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('Mouse Logitech MX Master', 'Mouse inalámbrico ergonómico con sensor de alta precisión', 79.99, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('Teclado Mecánico RGB', 'Teclado mecánico retroiluminado con switches Cherry MX', 129.99, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('Auriculares Sony WH-1000XM4', 'Auriculares inalámbricos con cancelación de ruido', 349.99, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('Monitor Samsung 27"', 'Monitor LED Full HD 27 pulgadas con tecnología IPS', 299.99, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),

-- Ropa (category_id = 2)
('Camiseta Nike Dri-FIT', 'Camiseta deportiva de alta tecnología que absorbe la humedad', 29.99, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
('Jeans Levi''s 501', 'Jeans clásicos de corte recto en denim de algodón', 89.99, 75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
('Zapatillas Adidas Ultraboost', 'Zapatillas para running con tecnología Boost', 179.99, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
('Chaqueta The North Face', 'Chaqueta impermeable para actividades al aire libre', 199.99, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),

-- Hogar (category_id = 3)
('Cafetera Nespresso', 'Cafetera de cápsulas con sistema de presión automático', 149.99, 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
('Aspiradora Roomba', 'Aspiradora robot inteligente con mapeo de habitaciones', 399.99, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
('Juego de Sábanas Queen', 'Sábanas de algodón egipcio 100% con 600 hilos', 79.99, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),

-- Deportes (category_id = 4)
('Pelota de Fútbol Adidas', 'Balón oficial de fútbol con certificación FIFA', 49.99, 80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),
('Mancuernas Ajustables 20kg', 'Set de mancuernas ajustables de 5 a 20 kg', 159.99, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),
('Colchoneta de Yoga', 'Colchoneta antideslizante de 6mm de grosor', 39.99, 70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),

-- Libros (category_id = 5)
('El Quijote - Miguel de Cervantes', 'Edición completa ilustrada del clásico español', 24.99, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),
('Clean Code - Robert C. Martin', 'Guía de buenas prácticas para escribir código limpio', 44.99, 65, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),
('Cien Años de Soledad - Gabriel García Márquez', 'Obra maestra del realismo mágico', 19.99, 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

-- =====================================================
-- Insertar Clientes
-- =====================================================
INSERT INTO customers (first_name, last_name, email, phone, address, birth_date, registration_date, status) VALUES
('Juan', 'Pérez García', 'juan.perez@email.com', '+34 600 123 456', 'Calle Mayor 15, Madrid', '1985-05-15', '2023-01-10', 'ACTIVE'),
('María', 'López Fernández', 'maria.lopez@email.com', '+34 611 234 567', 'Avenida Libertad 42, Barcelona', '1990-08-22', '2023-02-14', 'ACTIVE'),
('Carlos', 'Martínez Sánchez', 'carlos.martinez@email.com', '+34 622 345 678', 'Plaza España 8, Valencia', '1988-11-30', '2023-03-05', 'ACTIVE'),
('Ana', 'González Ruiz', 'ana.gonzalez@email.com', '+34 633 456 789', 'Calle Sol 23, Sevilla', '1995-03-12', '2023-04-20', 'ACTIVE'),
('Pedro', 'Rodríguez Torres', 'pedro.rodriguez@email.com', '+34 644 567 890', 'Avenida Constitución 67, Bilbao', '1982-07-18', '2023-05-15', 'INACTIVE');

-- =====================================================
-- Insertar Pedidos
-- =====================================================
INSERT INTO orders (order_number, order_date, status, total_amount, shipping_address, notes, customer_id) VALUES
('ORD-2024-001', '2024-01-15 10:30:00', 'DELIVERED', 1229.97, 'Calle Mayor 15, Madrid', 'Entrega rápida solicitada', 1),
('ORD-2024-002', '2024-02-20 14:45:00', 'DELIVERED', 269.98, 'Avenida Libertad 42, Barcelona', NULL, 2),
('ORD-2024-003', '2024-03-10 09:15:00', 'SHIPPED', 549.98, 'Plaza España 8, Valencia', 'Regalo de cumpleaños, envolver', 3),
('ORD-2024-004', '2024-04-05 16:20:00', 'PROCESSING', 89.98, 'Calle Sol 23, Sevilla', NULL, 4),
('ORD-2024-005', '2024-05-12 11:00:00', 'PENDING', 599.98, 'Calle Mayor 15, Madrid', 'Segundo pedido del cliente', 1);

-- =====================================================
-- Insertar Items de Pedidos
-- =====================================================

-- Pedido ORD-2024-001 (customer_id = 1)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(1, 1, 1, 899.99, 899.99),  -- Laptop HP Pavilion
(1, 2, 1, 79.99, 79.99),    -- Mouse Logitech
(1, 3, 1, 129.99, 129.99),  -- Teclado Mecánico
(1, 5, 1, 299.99, 299.99);  -- Monitor Samsung

-- Pedido ORD-2024-002 (customer_id = 2)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(2, 6, 3, 29.99, 89.97),    -- Camisetas Nike (x3)
(2, 7, 2, 89.99, 179.98);   -- Jeans Levi's (x2)

-- Pedido ORD-2024-003 (customer_id = 3)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(3, 4, 1, 349.99, 349.99),  -- Auriculares Sony
(3, 8, 1, 179.99, 179.99),  -- Zapatillas Adidas
(3, 13, 1, 49.99, 49.99);   -- Pelota de Fútbol

-- Pedido ORD-2024-004 (customer_id = 4)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(4, 16, 2, 24.99, 49.98),   -- El Quijote (x2)
(4, 18, 2, 19.99, 39.98);   -- Cien Años de Soledad (x2)

-- Pedido ORD-2024-005 (customer_id = 1)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(5, 10, 1, 149.99, 149.99), -- Cafetera Nespresso
(5, 11, 1, 399.99, 399.99), -- Aspiradora Roomba
(5, 15, 1, 39.99, 39.99);   -- Colchoneta de Yoga

-- =====================================================
-- Verificación de Datos (comentarios)
-- =====================================================
-- Este script inserta:
-- - 5 categorías de productos
-- - 18 productos distribuidos en las categorías
-- - 5 clientes con diferentes estados
-- - 5 pedidos en diferentes estados de procesamiento
-- - 14 items de pedidos que relacionan los pedidos con productos
--
-- Las relaciones demuestran:
-- - One-to-Many: Categoría -> Productos
-- - One-to-Many: Cliente -> Pedidos
-- - Many-to-One: Producto -> Categoría
-- - Many-to-One: Pedido -> Cliente
-- - Many-to-One: OrderItem -> Pedido y Producto
--
-- Los datos incluyen validación de:
-- - Emails únicos para clientes
-- - Números de pedido únicos
-- - Precios y cantidades positivas
-- - Estados válidos (enumeraciones)
-- - Integridad referencial entre tablas

