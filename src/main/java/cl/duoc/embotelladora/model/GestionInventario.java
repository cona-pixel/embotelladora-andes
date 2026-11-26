package cl.duoc.embotelladora.model;

public interface GestionInventario {


    void agregarStock(int cantidad);
    void descontarStock(int cantidad);
    int getStock();

}
