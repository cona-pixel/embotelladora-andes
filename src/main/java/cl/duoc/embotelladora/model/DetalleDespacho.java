package cl.duoc.embotelladora.model;

public class DetalleDespacho {

    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetalleDespacho(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto del detalle es obligatorio.");
        }
        setProducto(producto);
        setCantidad(cantidad);
        this.precioUnitario = producto.calcularPrecio();
    }
    public DetalleDespacho(Producto producto, int cantidad, double precioUnitario) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto del detalle es obligatorio.");
        }
        if (precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario del detalle debe ser mayor que cero.");
        }
        this.producto = producto;
        setCantidad(cantidad);
        this.precioUnitario = precioUnitario;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del detalle debe ser mayor que cero.");
        }
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double calcularSubtotal(){ return cantidad * precioUnitario;}

    public void validarCantidad() {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad solicitada debe ser mayor que cero.");
        }
        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para " + producto.getCodigo() + " (" + producto.getNombre()
                            + "). Disponible: " + producto.getStock() + ", solicitado: " + cantidad + ".");
        }
    }

    public String getCodigoProducto(){ return producto.getCodigo();}

    public String getNombreProducto(){ return  producto.getNombre();}

    public String getPrecioFormateado(){ return String.format("$%,.0f", precioUnitario).replace(',','.');}

    public String getSubtotalFormateado(){ return String.format("$%,.0f",calcularSubtotal()).replace(',','.');}


}


