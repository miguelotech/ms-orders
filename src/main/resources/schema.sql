CREATE TABLE IF NOT EXISTS pedidos (
    id SERIAL PRIMARY KEY,
    cliente VARCHAR(255),
    fecha TIMESTAMP,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS detalles_pedido (
    id SERIAL PRIMARY KEY,
    pedido_id BIGINT,
    producto_id BIGINT,
    cantidad INTEGER,
    precio_unitario NUMERIC(10,2),
    subtotal NUMERIC(10,2),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);
