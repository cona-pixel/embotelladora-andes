# Modelo Entidad-Relación (MER) — Embotelladora Los Andes (3FN)

Coherente con `scripts/embotelladora_mysql.sql`.

```mermaid
erDiagram
    tipo_producto {
        INT id_tipo PK
        VARCHAR descripcion UK
    }

    producto {
        VARCHAR codigo PK
        INT id_tipo FK
        VARCHAR nombre
        DECIMAL precio_unitario
        INT stock
        INT volumen_ml
    }

    bebida_carbonatada {
        VARCHAR codigo_producto PK, FK
        DECIMAL nivel_gasificacion
        VARCHAR sabor
    }

    bebida_natural {
        VARCHAR codigo_producto PK, FK
        DECIMAL porcentaje_fruta
        BOOLEAN contiene_conservantes
    }

    agua_embotellada {
        VARCHAR codigo_producto PK, FK
        VARCHAR tipo_agua
        VARCHAR origen
    }

    cliente {
        VARCHAR rut PK
        VARCHAR nombre
        VARCHAR direccion
        VARCHAR telefono
    }

    despacho {
        VARCHAR numero_despacho PK
        DATE fecha
        VARCHAR rut_cliente FK
        VARCHAR estado
    }

    detalle_despacho {
        INT id_detalle PK
        VARCHAR numero_despacho FK
        VARCHAR codigo_producto FK
        INT cantidad
        DECIMAL precio_unitario
    }

    tipo_producto ||--o{ producto : clasifica
    producto ||--o| bebida_carbonatada : especializa
    producto ||--o| bebida_natural : especializa
    producto ||--o| agua_embotellada : especializa
    cliente ||--o{ despacho : recibe
    despacho ||--|{ detalle_despacho : contiene
    producto ||--o{ detalle_despacho : incluido_en
```

## Justificación de Normalización (3FN)

1. **1FN:** Todos los atributos son atómicos, cada tabla tiene `PRIMARY KEY` y los ítems de cada despacho se separan en `detalle_despacho`.
2. **2FN:** No existen dependencias parciales porque todas las tablas tienen clave primaria simple (`codigo`, `rut`, `numero_despacho`, `id_detalle`).
3. **3FN:**
    - Los atributos propios de cada tipo de bebida se separan en 3 tablas 1:1 (`bebida_carbonatada`, `bebida_natural`, `agua_embotellada`), eliminando columnas `NULL` dependientes del tipo.
    - `tipo_producto` evita repetir nombres de categorías en `producto`.
    - No se almacenan campos calculados (`total`, `subtotal`, `total_unidades`).
    - `detalle_despacho.precio_unitario` guarda el precio histórico congelado del despacho (depende de `id_detalle`, no transitivamente de `producto`).
    - `ON DELETE RESTRICT` impide borrar productos o clientes que tengan despachos asociados.