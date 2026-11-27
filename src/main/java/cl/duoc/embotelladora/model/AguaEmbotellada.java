package cl.duoc.embotelladora.model;

public class AguaEmbotellada extends Producto{

    public static final String TIPO = "Agua Embotellada";
    public static final String TIPO_PURIFICADA = "Purificada";
    public static final String TIPO_MINERAL = "Mineral";
    public static final String TIPO_SABORIZADA = "Saborizada";

    private String tipoAgua;
    private String origen;

    public AguaEmbotellada(String codigo, String nombre, double precioUnitario, int stock, int volumenML,
                           String tipoAgua, String origen) {
        super(codigo, nombre, precioUnitario, stock, volumenML);
        setTipoAgua(tipoAgua);
        setOrigen(origen);
    }

    public String getTipoAgua() {
        return tipoAgua;
    }

    public void setTipoAgua(String tipoAgua) {
        if(tipoAgua == null || tipoAgua.trim().isEmpty()){
            throw new IllegalArgumentException("El tipo de agua es obligatorio.");
        }
        String valor = tipoAgua.trim();
        if(!TIPO_PURIFICADA.equalsIgnoreCase(valor) && !TIPO_MINERAL.equalsIgnoreCase(valor) && !TIPO_SABORIZADA.equalsIgnoreCase(valor)){
            throw new IllegalArgumentException("El tipo de agua debe ser Purificada,Mineral o Saborizada.");
        }
        if (TIPO_PURIFICADA.equalsIgnoreCase(valor)) {
            this.tipoAgua = TIPO_PURIFICADA;
        } else if (TIPO_MINERAL.equalsIgnoreCase(valor)) {
            this.tipoAgua = TIPO_MINERAL;
        }else{
            this.tipoAgua = TIPO_SABORIZADA;
        }
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        if(origen == null || origen.trim().isEmpty()){
            throw new IllegalArgumentException("El origen del agua es obligatorio.");
        }
        this.origen = origen.trim();
    }



    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularPrecio() {
        if(TIPO_MINERAL.equalsIgnoreCase(tipoAgua)){
            return getPrecioUnitario() * 1.15;
        }
        if(TIPO_SABORIZADA.equalsIgnoreCase(tipoAgua)){
            return getPrecioUnitario() * 1.20;
        }
        return getPrecioUnitario();
    }

    @Override
    public String obtenerDescripcion() {
        return "Agua " + tipoAgua + ", origen " + origen + ", " + getVolumenMl() + " ml" ;
    }
}
