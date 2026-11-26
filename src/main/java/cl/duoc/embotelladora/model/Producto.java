package cl.duoc.embotelladora.model;

public abstract class Producto  implements GestionInventario{

    private String codigo;
    private String nombre;
    private double precioUnitario;
    private int stock;
    private int volumenML;

    public Producto(String codigo, String nombre, double precioUnitario, int stock, int volumenML) {
        setCodigo(codigo);
        setNombre(nombre);
        setPrecioUnitario(precioUnitario);
        setStock(stock);
        setVolumenML(volumenML);
    }

    public String getCodigo(){
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()){
            throw new IllegalArgumentException("El codigo del producto es obligatorio.");
        }
        this.codigo = codigo.trim().toUpperCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if(precioUnitario <= 0){
            throw new IllegalArgumentException("El precio unitario debe ser mayor que cero.");
        }
        this.precioUnitario = precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if(stock < 0){
            throw new IllegalArgumentException("El stock inicial no puede ser negativo.");
        }
        this.stock = stock;
    }

    public int getVolumenML() {
        return volumenML;
    }

    public void setVolumenML(int volumenML) {
        if(volumenML <= 0){
            throw new IllegalArgumentException("El volumen (ml) debe ser mayor que cero.");
        }

        this.volumenML = volumenML;
    }

    @Override
    public void agregarStock(int cantidad) {
        if(cantidad <= 0){
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor que cero.");
        }
        this.stock += cantidad;
    }

    public void agregarStock(int cajas, int unidadesPorCaja){
        if (cajas <= 0 || unidadesPorCaja <= 0){
            throw new IllegalArgumentException("Las cajas y unidades por caja deben ser mayores que cero.");
        }
        agregarStock(cajas * unidadesPorCaja);
    }

    @Override
    public void descontarStock(int cantidad) {

        if (cantidad <= 0){
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor que cero.");
        }
        if (cantidad > this.stock){
            throw new IllegalArgumentException("Stock insuficiente para " + codigo + "(" + nombre + "). Disponible:"
                                                    +stock+ ", solicitado: " + cantidad + ".");
        }
        this.stock -= cantidad;
    }


    public abstract String getTipo();

    public abstract double calcularPrecio();

    public abstract String obtenerDescripcion();

    public String getPrecioBaseFormateado(){ return String.format("$%,.0f", precioUnitario).replace(',','.');}
    public String getPrecioFinalFormateado(){return String.format("$%,.0f", calcularPrecio()).replace(',','.');}
    public String getDescripcionDetalle(){ return obtenerDescripcion();}

    @Override
    public String toString(){ return codigo + " - " + nombre + " (Stock: " + stock + ")";
    }
}
