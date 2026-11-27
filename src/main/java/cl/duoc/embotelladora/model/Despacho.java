package cl.duoc.embotelladora.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Despacho {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    private String numeroDespacho;
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<DetalleDespacho> detalles;
    private String estado;

    public Despacho(String numeroDespacho, LocalDate fecha, Cliente cliente) {
        setNumeroDespacho(numeroDespacho);
        setFecha(fecha);
        setCliente(cliente);
        this.detalles = new ArrayList<>();
        this.estado = ESTADO_PENDIENTE;
    }

    public Despacho(String numeroDespacho, LocalDate fecha, Cliente cliente,
                    String estado, List<DetalleDespacho> detallesExistentes) {
        setNumeroDespacho(numeroDespacho);
        setFecha(fecha);
        setCliente(cliente);
        this.estado = (estado != null) ? estado : ESTADO_PENDIENTE;
        this.detalles = new ArrayList<>();
        if (detallesExistentes != null) {
            this.detalles.addAll(detallesExistentes);
        }
    }

    public String getNumeroDespacho() {
        return numeroDespacho;
    }

    public void setNumeroDespacho(String numeroDespacho) {
        if (numeroDespacho == null || numeroDespacho.trim().isEmpty()) {
            throw new IllegalArgumentException("El numero de despacho es obligatorio.");
        }
        this.numeroDespacho = numeroDespacho.trim().toUpperCase();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del despacho es obligatoria.");
        }
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente para el despacho.");
        }
        this.cliente = cliente;
    }

    public List<DetalleDespacho> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public String getEstado() {
        return estado;
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        if (!ESTADO_PENDIENTE.equals(estado)) {
            throw new IllegalArgumentException("Solo se pueden modificar despachos en estado PENDIENTE.");
        }
        if (producto == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto valido.");
        }
        for (DetalleDespacho existente : detalles) {
            if (existente.getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                existente.setCantidad(existente.getCantidad() + cantidad);
                return;
            }
        }
        detalles.add(new DetalleDespacho(producto, cantidad));
    }

    public double calcularTotal() {
        double total = 0;
        for (DetalleDespacho d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }

    public int calcularTotalUnidades() {
        int totalUnidades = 0;
        for (DetalleDespacho d : detalles) {
            totalUnidades += d.getCantidad();
        }
        return totalUnidades;
    }

    public void confirmarDespacho() {
        if (ESTADO_CANCELADO.equals(estado)) {
            throw new IllegalArgumentException("Un despacho cancelado no puede volver a confirmarse.");
        }
        if (ESTADO_CONFIRMADO.equals(estado)) {
            throw new IllegalArgumentException("El despacho ya se encuentra confirmado.");
        }
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("Un despacho debe contener al menos un producto.");
        }

        // 1) Validar stock de TODOS los productos antes de descontar ninguno
        for (DetalleDespacho d : detalles) {
            d.validarCantidad();
        }

        // 2) Solo si todos cumplen, descontar stock y confirmar
        for (DetalleDespacho d : detalles) {
            d.getProducto().descontarStock(d.getCantidad());
        }

        this.estado = ESTADO_CONFIRMADO;
    }

    public void cancelarDespacho() {
        if (ESTADO_CONFIRMADO.equals(estado)) {
            throw new IllegalArgumentException("Un despacho confirmado no puede cancelarse ni modificarse.");
        }
        if (ESTADO_CANCELADO.equals(estado)) {
            throw new IllegalArgumentException("El despacho ya esta cancelado.");
        }
        this.estado = ESTADO_CANCELADO;
    }

    public String getNombreCliente() {
        return cliente.getNombre();
    }

    public int getTotalUnidades() {
        return calcularTotalUnidades();
    }

    public String getTotalFormateado() {
        return String.format("$%,.0f", calcularTotal()).replace(',', '.');
    }
}