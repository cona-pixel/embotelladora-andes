DROP DATABASE IF EXISTS embotelladora;
CREATE DATABASE embotelladora CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE embotelladora;

CREATE TABLE tipo_producto (
    id_tipo     INT         NOT NULL,
    descripcion VARCHAR(30) NOT NULL,
    CONSTRAINT pk_tipo_producto PRIMARY KEY (id_tipo),
    CONSTRAINT uq_tipo_producto_descripcion UNIQUE (descripcion)
);

CREATE TABLE producto (
    codigo          VARCHAR(30)   NOT NULL,
    id_tipo         INT           NOT NULL,
    nombre          VARCHAR(100)  NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    stock           INT           NOT NULL,
    volumen_ml      INT           NOT NULL,
    CONSTRAINT pk_producto PRIMARY KEY (codigo),
    CONSTRAINT fk_producto_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_producto (id_tipo),
    CONSTRAINT ck_producto_precio CHECK (precio_unitario > 0),
    CONSTRAINT ck_producto_stock CHECK (stock >= 0),
    CONSTRAINT ck_producto_volumen CHECK (volumen_ml > 0)
);

CREATE TABLE bebida_carbonatada (
    codigo_producto    VARCHAR(30)  NOT NULL,
    nivel_gasificacion DECIMAL(5,2) NOT NULL,
    sabor              VARCHAR(60)  NOT NULL,
    CONSTRAINT pk_bebida_carbonatada PRIMARY KEY (codigo_producto),
    CONSTRAINT fk_bc_producto FOREIGN KEY (codigo_producto)
        REFERENCES producto (codigo) ON DELETE CASCADE,
    CONSTRAINT ck_bc_gasificacion CHECK (nivel_gasificacion > 0)
);

CREATE TABLE bebida_natural (
    codigo_producto       VARCHAR(30)  NOT NULL,
    porcentaje_fruta      DECIMAL(5,2) NOT NULL,
    contiene_conservantes BOOLEAN      NOT NULL,
    CONSTRAINT pk_bebida_natural PRIMARY KEY (codigo_producto),
    CONSTRAINT fk_bn_producto FOREIGN KEY (codigo_producto)
        REFERENCES producto (codigo) ON DELETE CASCADE,
    CONSTRAINT ck_bn_fruta CHECK (porcentaje_fruta > 0 AND porcentaje_fruta <= 100)
);

CREATE TABLE agua_embotellada (
    codigo_producto VARCHAR(30) NOT NULL,
    tipo_agua       VARCHAR(20) NOT NULL,
    origen          VARCHAR(80) NOT NULL,
    CONSTRAINT pk_agua_embotellada PRIMARY KEY (codigo_producto),
    CONSTRAINT fk_ae_producto FOREIGN KEY (codigo_producto)
        REFERENCES producto (codigo) ON DELETE CASCADE,
    CONSTRAINT ck_ae_tipo_agua CHECK (tipo_agua IN ('Purificada', 'Mineral', 'Saborizada'))
);

CREATE TABLE cliente (
    rut       VARCHAR(20)  NOT NULL,
    nombre    VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    telefono  VARCHAR(30)  NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (rut)
);

CREATE TABLE despacho (
    numero_despacho VARCHAR(30) NOT NULL,
    fecha           DATE        NOT NULL,
    rut_cliente     VARCHAR(20) NOT NULL,
    estado          VARCHAR(20) NOT NULL,
    CONSTRAINT pk_despacho PRIMARY KEY (numero_despacho),
    CONSTRAINT fk_despacho_cliente FOREIGN KEY (rut_cliente)
        REFERENCES cliente (rut) ON DELETE RESTRICT,
    CONSTRAINT ck_despacho_estado CHECK (estado IN ('PENDIENTE', 'CONFIRMADO', 'CANCELADO'))
);

CREATE TABLE detalle_despacho (
    id_detalle      INT           NOT NULL AUTO_INCREMENT,
    numero_despacho VARCHAR(30)   NOT NULL,
    codigo_producto VARCHAR(30)   NOT NULL,
    cantidad        INT           NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_detalle_despacho PRIMARY KEY (id_detalle),
    CONSTRAINT uq_detalle_despacho_prod UNIQUE (numero_despacho, codigo_producto),
    CONSTRAINT fk_detalle_despacho FOREIGN KEY (numero_despacho)
        REFERENCES despacho (numero_despacho) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (codigo_producto)
        REFERENCES producto (codigo) ON DELETE RESTRICT,
    CONSTRAINT ck_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_detalle_precio CHECK (precio_unitario > 0)
);

-- Datos iniciales de prueba (B001 a B004 y 2 clientes)
INSERT INTO tipo_producto (id_tipo, descripcion) VALUES
    (1, 'Bebida Carbonatada'),
    (2, 'Bebida Natural'),
    (3, 'Agua Embotellada');

INSERT INTO producto (codigo, id_tipo, nombre, precio_unitario, stock, volumen_ml) VALUES
    ('B001', 1, 'Cola clasica',    1200.00,  500,  500),
    ('B002', 1, 'Naranja',         1100.00,  300,  500),
    ('B003', 2, 'Jugo de manzana', 1500.00,  200, 1000),
    ('B004', 3, 'Agua mineral',     800.00, 1000,  600);

INSERT INTO bebida_carbonatada (codigo_producto, nivel_gasificacion, sabor) VALUES
    ('B001', 3.50, 'Cola'),
    ('B002', 2.80, 'Naranja');

INSERT INTO bebida_natural (codigo_producto, porcentaje_fruta, contiene_conservantes) VALUES
    ('B003', 70.00, FALSE);

INSERT INTO agua_embotellada (codigo_producto, tipo_agua, origen) VALUES
    ('B004', 'Mineral', 'Cordillera de los Andes');

INSERT INTO cliente (rut, nombre, direccion, telefono) VALUES
    ('76.123.456-7', 'Distribuidora Cordillera SpA', 'Av. Providencia 1450, Santiago', '+56 2 2456 7890'),
    ('77.987.654-3', 'Supermercados Valle Central Ltda.', 'Calle Libertad 320, Vina del Mar', '+56 32 298 1122');
