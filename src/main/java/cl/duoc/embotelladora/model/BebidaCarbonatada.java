package cl.duoc.embotelladora.model;
public class BebidaCarbonatada extends Producto{

    public static final String TIPO = "Bebida Carbonatada";

    private double nivelGasificacion;
    private String sabor;

    public BebidaCarbonatada(String codigo, String nombre,
                             double precioUnitario, int stock, int volumenML, double nivelGasificacion,String sabor)
    {
        super(codigo, nombre, precioUnitario, stock, volumenML);
        setNivelGasificacion(nivelGasificacion);
        setSabor(sabor);
    }

    public double getNivelGasificacion(){
        return nivelGasificacion;
    }
    public void setNivelGasificacion(double nivelGasificacion){
        if (nivelGasificacion <= 0){
            throw new IllegalArgumentException("El nivel de gasificacion debe ser mayor que cero.");
        }
        this.nivelGasificacion = nivelGasificacion;
    }

    public String getSabor(){
        return sabor;
    }

    public void setSabor(String sabor) {
        if (sabor == null || sabor.trim().isEmpty()){
            throw new IllegalArgumentException("El sabor es obligatorio.");
        }
        this.sabor = sabor.trim();
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularPrecio() {
        if (nivelGasificacion > 3.0){
            return getPrecioUnitario() * 1.15;
        }
        return getPrecioUnitario() * 1.10;
    }

    @Override
    public String obtenerDescripcion() {
        return "Sabor " + sabor + ", gas " + nivelGasificacion + " vol, " + getVolumenMl() + " ml";

    }
}

