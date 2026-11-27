package cl.duoc.embotelladora.dao;

import cl.duoc.embotelladora.model.Producto;

import java.util.List;

public interface ProductoDAO {

    List<Producto> obtenerTodos()throws PersistenciaException;

    Producto buscarPorCodigo(String codigo) throws PersistenciaException;


    void insertar(Producto producto) throws PersistenciaException;

    void actualizar(Producto producto) throws PersistenciaException;

    void actualizarStock(String codigo, int nuevoStock) throws PersistenciaException;

    void eliminar(String codigo) throws PersistenciaException;

}
