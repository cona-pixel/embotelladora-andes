package cl.duoc.embotelladora.model;

public class BebidaNatural extends Producto {

    public static final String TIPO = "Bebida Natural";

    private double porcentajeFruta;
    private boolean contieneConservantes;

    public BebidaNatural(String codigo, String nombre, double precioUnitario, int stock, int volumenML, double porcentajeFruta, boolean contieneConservantes) {
        super(codigo, nombre, precioUnitario, stock, volumenML);

        setPorcentajeFruta(porcentajeFruta);
        this.contieneConservantes = contieneConservantes;
    }

    public double getPorcentajeFruta(){
        return porcentajeFruta;
    }

    public void setPorcentajeFruta(double porcentajeFruta) {
        if(porcentajeFruta <= 0 || porcentajeFruta > 100){
            throw new IllegalArgumentException("El porcentaje de fruta debe estar entre 1 y 100.");
        }
        this.porcentajeFruta = porcentajeFruta;
    }
    public boolean isContieneConservantes(){
        return contieneConservantes;
    }

    public void setContieneConservantes(boolean contieneConservantes) {
        this.contieneConservantes = contieneConservantes;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularPrecio() {
        if (porcentajeFruta >= 50.0){
            return getPrecioUnitario() * 1.20;
        }
        return getPrecioUnitario() * 1.10;
    }

    @Override
    public String obtenerDescripcion() {
        String conservantes = contieneConservantes ? "con conservantes" : "sin conservantes";
        return porcentajeFruta + "% fruta, " + conservantes + ", " + getVolumenMl() + " ml";
    }
}
