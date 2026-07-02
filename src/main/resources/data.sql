-- =============================================================================
-- AgroConnect - Datos de prueba dinámicos para Swagger y OWASP ZAP
-- Este script inserta datos en carrito, pedidos, reseñas y notificaciones
-- basándose en los usuarios y productos ya existentes en la base de datos.
-- =============================================================================

DO $$
DECLARE
    v_comprador1_id BIGINT;
    v_comprador2_id BIGINT;
    v_productor_id BIGINT;
    v_producto1_id BIGINT;
    v_producto2_id BIGINT;
    v_producto3_id BIGINT;
    v_categoria_id BIGINT;
    v_pedido1_id BIGINT;
    v_pedido2_id BIGINT;
BEGIN
    -- 1. Obtener IDs de usuarios existentes
    -- Intentar obtener usuarios con rol 'comprador'
    SELECT id_usuario INTO v_comprador1_id FROM usuarios WHERE LOWER(rol) = 'comprador' LIMIT 1;
    SELECT id_usuario INTO v_comprador2_id FROM usuarios WHERE LOWER(rol) = 'comprador' OFFSET 1 LIMIT 1;
    
    -- Si no hay compradores, usar los primeros usuarios disponibles
    IF v_comprador1_id IS NULL THEN
        SELECT id_usuario INTO v_comprador1_id FROM usuarios LIMIT 1;
    END IF;
    IF v_comprador2_id IS NULL THEN
        SELECT id_usuario INTO v_comprador2_id FROM usuarios OFFSET 1 LIMIT 1;
    END IF;
    
    -- Obtener un productor
    SELECT id_usuario INTO v_productor_id FROM usuarios WHERE LOWER(rol) = 'productor' LIMIT 1;
    IF v_productor_id IS NULL THEN
        SELECT id_usuario INTO v_productor_id FROM usuarios LIMIT 1;
    END IF;

    -- 2. Obtener IDs de productos existentes
    SELECT id_producto INTO v_producto1_id FROM productos LIMIT 1;
    SELECT id_producto INTO v_producto2_id FROM productos OFFSET 1 LIMIT 1;
    SELECT id_producto INTO v_producto3_id FROM productos OFFSET 2 LIMIT 1;

    -- 3. Obtener o crear una categoría si es necesario
    SELECT id_categoria INTO v_categoria_id FROM categorias LIMIT 1;
    IF v_categoria_id IS NULL THEN
        INSERT INTO categorias (nombre, descripcion, icono)
        VALUES ('General', 'Categoría general de productos', '📦')
        RETURNING id_categoria INTO v_categoria_id;
    END IF;

    -- Si tenemos al menos un usuario y un producto, procedemos a insertar los datos de prueba
    IF v_comprador1_id IS NOT NULL AND v_producto1_id IS NOT NULL THEN

        -- ==========================================
        -- A. NOTIFICACIONES DE PRUEBA
        -- ==========================================
        IF NOT EXISTS (SELECT 1 FROM notificaciones) THEN
            INSERT INTO notificaciones (id_usuario, mensaje, tipo, leida, fecha_creacion) VALUES
            (v_comprador1_id, '¡Bienvenido a AgroConnect! Tu cuenta ha sido activada con éxito.', 'info', false, NOW()),
            (v_comprador1_id, 'Tu pedido #1 ha sido enviado por el productor.', 'pedido', false, NOW() - INTERVAL '1 day'),
            (COALESCE(v_comprador2_id, v_comprador1_id), 'Hay una oferta especial en productos de temporada.', 'promo', true, NOW() - INTERVAL '2 days');
        END IF;

        -- ==========================================
        -- B. CARRITO DE PRUEBA
        -- ==========================================
        IF NOT EXISTS (SELECT 1 FROM carrito) THEN
            INSERT INTO carrito (id_usuario, id_producto, cantidad, fecha_agregado) VALUES
            (v_comprador1_id, v_producto1_id, 2, NOW()),
            (v_comprador1_id, COALESCE(v_producto2_id, v_producto1_id), 1, NOW()),
            (COALESCE(v_comprador2_id, v_comprador1_id), v_producto1_id, 5, NOW());
        END IF;

        -- ==========================================
        -- C. PEDIDOS Y DETALLES DE PRUEBA
        -- ==========================================
        IF NOT EXISTS (SELECT 1 FROM pedidos) THEN
            -- Pedido 1
            INSERT INTO pedidos (id_comprador, estado, total, fecha_pedido)
            VALUES (v_comprador1_id, 'pendiente', 25000.00, NOW() - INTERVAL '3 hours')
            RETURNING id_pedido INTO v_pedido1_id;

            INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario)
            VALUES (v_pedido1_id, v_producto1_id, 2, 10000.00);

            IF v_producto2_id IS NOT NULL THEN
                INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario)
                VALUES (v_pedido1_id, v_producto2_id, 1, 5000.00);
            END IF;

            -- Pedido 2
            INSERT INTO pedidos (id_comprador, estado, total, fecha_pedido)
            VALUES (COALESCE(v_comprador2_id, v_comprador1_id), 'completado', 12000.00, NOW() - INTERVAL '2 days')
            RETURNING id_pedido INTO v_pedido2_id;

            INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario)
            VALUES (v_pedido2_id, v_producto1_id, 1, 12000.00);
        END IF;

        -- ==========================================
        -- D. RESEÑAS DE PRUEBA
        -- ==========================================
        IF NOT EXISTS (SELECT 1 FROM resenas) THEN
            INSERT INTO resenas (id_producto, id_comprador, calificacion, comentario, fecha_resena) VALUES
            (v_producto1_id, v_comprador1_id, 5, 'Excelente calidad, el producto llegó muy fresco y a tiempo.', NOW() - INTERVAL '1 day'),
            (v_producto1_id, COALESCE(v_comprador2_id, v_comprador1_id), 4, 'Muy buen producto, aunque el empaque podría mejorar.', NOW() - INTERVAL '12 hours');
        END IF;

    END IF;
END $$;
