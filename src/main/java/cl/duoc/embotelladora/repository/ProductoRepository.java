package cl.duoc.embotelladora.repository;

import cl.duoc.embotelladora.dao.PersistenciaException;
import cl.duoc.embotelladora.dao.ProductoDAO;
import cl.duoc.embotelladora.model.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {

    private final ProductoDAO productoDAO;
    private final ArrayList<Producto> productos;

    public ProductoRepository(ProductoDAO productoDAO) {
        if (productoDAO == null) {
            throw new IllegalArgumentException("El ProductoDAO es obligatorio.");
        }
        this.productoDAO = productoDAO;
        this.productos = new ArrayList<>();
    }

    public List<Producto> listarProductos() throws PersistenciaException {
        List<Producto> desdeBD = productoDAO.obtenerTodos();
        productos.clear();
        productos.addAll(desdeBD);
        return new ArrayList<>(productos);
    }

    public Producto buscarPorCodigo(String codigo) throws PersistenciaException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar un código de producto.");
        }
        Producto encontrado = productoDAO.buscarPorCodigo(codigo);
        if (encontrado == null) {
            throw new IllegalArgumentException("No existe un producto con el código" + codigo.trim().toUpperCase() + ".");
        }
        return encontrado;
    }

    public void registrar(Producto producto) throws PersistenciaException {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (productoDAO.buscarPorCodigo(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto registrado con el codigo" + producto.getCodigo() + ".");
        }
        productoDAO.insertar(producto);
        productos.add(producto);
    }

    public void actualizar(Producto producto) throws PersistenciaException {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        productoDAO.actualizar(producto);
        listarProductos();
    }

    public void eliminar(String codigo) throws PersistenciaException {
        productoDAO.eliminar(codigo);
        listarProductos();
    }

    public void agregarStock(String codigo, int cantidad) throws PersistenciaException {
        Producto producto = buscarPorCodigo(codigo);
        producto.agregarStock(cantidad);
        productoDAO.actualizarStock(producto.getCodigo(), producto.getStock());
        listarProductos();
    }

    public void agregarStockPorCajas(String codigo, int cajas, int unidadesPorCaja) throws PersistenciaException {
        Producto producto = buscarPorCodigo(codigo);
        producto.agregarStock(cajas, unidadesPorCaja);
        productoDAO.actualizarStock(producto.getCodigo(), producto.getStock());
        listarProductos();
    }

    public void descontarStock(String codigo, int cantidad) throws PersistenciaException {
        Producto producto = buscarPorCodigo(codigo);
        producto.descontarStock(cantidad);
        productoDAO.actualizarStock(producto.getCodigo(), producto.getStock());
        listarProductos();
    }
}
