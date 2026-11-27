package cl.duoc.embotelladora.dao;

import cl.duoc.embotelladora.conexion.ConexionBD;
import cl.duoc.embotelladora.model.AguaEmbotellada;
import cl.duoc.embotelladora.model.BebidaCarbonatada;
import cl.duoc.embotelladora.model.BebidaNatural;
import cl.duoc.embotelladora.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoJdbcDAO implements  ProductoDAO{

    private static final int TIPO_CARBONATADA = 1;
    private static final int TIPO_NATURAL = 2;
    private static final int TIPO_AGUA = 3;

    private static final String SQL_SELECT_BASE =
            "SELECT p.codigo, p.id_tipo, p.nombre, p.precio_unitario, p.stock, p.volumen_ml, "
                    + "bc.nivel_gasificacion, bc.sabor, "
                    + "bn.porcentaje_fruta, bn.contiene_conservantes, "
                    + "ae.tipo_agua, ae.origen "
                    + "FROM producto p "
                    + "LEFT JOIN bebida_carbonatada bc ON p.codigo = bc.codigo_producto "
                    + "LEFT JOIN bebida_natural bn ON p.codigo = bn.codigo_producto "
                    + "LEFT JOIN agua_embotellada ae ON p.codigo = ae.codigo_producto ";

    @Override
    public List<Producto> obtenerTodos() throws PersistenciaException {
        String sql = SQL_SELECT_BASE + "ORDER BY p.codigo";
        List<Producto> productos = new ArrayList<>();

        try(Connection conexion = ConexionBD.getConnection();
        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                productos.add(mapearProducto(rs));
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudieron cargar los productos desde MySQL.", e);
        }
        return productos;
    }


    @Override
    public Producto buscarPorCodigo(String codigo) throws PersistenciaException {
        String sql = SQL_SELECT_BASE + "WHERE p.codigo = ?";

        try(Connection conexion = ConexionBD.getConnection();
            PreparedStatement ps =conexion.prepareStatement(sql)){

            ps.setString(1, codigo.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapearProducto(rs);
                }
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo buscar el producto por codigo.", e);
        }
        return null;
    }

    @Override
    public void insertar(Producto producto) throws PersistenciaException {
        String sqlBase = "INSERT INTO producto (codigo, id_tipo, nombre, precio_unitario, stock, volumen_ml) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConnection()) {
            try (PreparedStatement ps = conexion.prepareStatement(sqlBase)) {
                ps.setString(1, producto.getCodigo());
                ps.setInt(2, obtenerIdTipo(producto));
                ps.setString(3, producto.getNombre());
                ps.setDouble(4, producto.getPrecioUnitario());
                ps.setInt(5, producto.getStock());
                ps.setInt(6, producto.getVolumenMl());

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    throw new PersistenciaException("No se pudo registrar el producto en MySQL: ");
                }
            }
            insertarSubtabla(conexion, producto);

        }catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el producto en MySQL: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Producto producto) throws PersistenciaException {
        String sqlBase = "UPDATE producto SET nombre = ?, precio_unitario =?, stock =?, volumen_ml=? " + "WHERE codigo =?" ;

        try(Connection conexion = ConexionBD.getConnection()){
            try (PreparedStatement ps = conexion.prepareStatement(sqlBase)){
                ps.setString(1,producto.getNombre());
                ps.setDouble(2,producto.getPrecioUnitario());
                ps.setInt(3, producto.getStock());
                ps.setInt(4, producto.getVolumenMl());
                ps.setString(5, producto.getCodigo());

                int filas = ps.executeUpdate();
                if (filas == 0){
                    throw new PersistenciaException("No se pudo actualizar: el producto no existe.");
                }
            }
            actualizarSubtabla(conexion,producto);

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo actualizar el producto en MySQL.", e);
        }
    }

    @Override
    public void actualizarStock(String codigo, int nuevoStock) throws PersistenciaException {
        String sql = "UPDATE producto SET stock = ? WHERE codigo = ?";

        try (Connection conexion = ConexionBD.getConnection();
            PreparedStatement ps= conexion.prepareStatement(sql)){

            ps.setInt(1,nuevoStock);
            ps.setString(2,codigo.trim().toUpperCase());

            int filas =ps.executeUpdate();
            if (filas == 0){
                throw new PersistenciaException("No se pudo actualizar el stock: el producto no existe.");
            }
        }catch(SQLException e){
            throw new PersistenciaException("No se pudo actualizar el stock en MySQL.", e);
        }
    }

    @Override
    public void eliminar(String codigo) throws PersistenciaException {
        String sql = "DELETE FROM producto WHERE  codigo =?";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo.trim().toUpperCase());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No se pudo eliminar: el producto no existe.");
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar el producto (puede estar asociado a un despacho).", e);
        }
    }

    private void insertarSubtabla(Connection conexion, Producto producto) throws SQLException, PersistenciaException{
        if(producto instanceof BebidaCarbonatada){
            BebidaCarbonatada bc = (BebidaCarbonatada) producto;
            String sql = "INSERT INTO bebida_carbonatada (codigo_producto, nivel_gasificacion, sabor) VALUES (?,?,?)";

            try(PreparedStatement ps = conexion.prepareStatement(sql)){
                ps.setString(1,bc.getCodigo());
                ps.setDouble(2,bc.getNivelGasificacion());
                ps.setString(3,bc.getSabor());
                if(ps.executeUpdate() == 0){
                    throw new PersistenciaException("No se guardo la bebida carbonatada.");
                }
            }
        } else if (producto instanceof BebidaNatural) {
            BebidaNatural bn =(BebidaNatural) producto;
            String sql = "INSERT INTO bebida_natural (codigo_producto, porcentaje_fruta, contiene_conservantes) VALUES (?, ?, ?)";

            try(PreparedStatement ps = conexion.prepareStatement(sql)){
                ps.setString(1,bn.getCodigo());
                ps.setDouble(2,bn.getPorcentajeFruta());
                ps.setBoolean(3,bn.isContieneConservantes());
                if (ps.executeUpdate() == 0){
                    throw new PersistenciaException("No se guardo el agua embotellada.");

                }
            }

        } else if (producto instanceof AguaEmbotellada) {
            AguaEmbotellada ae = (AguaEmbotellada) producto;
            String sql = "INSERT INTO agua_embotellada (codigo_producto, tipo_agua, origen) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, ae.getCodigo());
                ps.setString(2, ae.getTipoAgua());
                ps.setString(3, ae.getOrigen());
                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se guardo el agua embotellada.");
                }
            }
        }
    }

    private void actualizarSubtabla (Connection conexion, Producto producto) throws SQLException, PersistenciaException {
        if (producto instanceof BebidaCarbonatada) {
            BebidaCarbonatada bc = (BebidaCarbonatada) producto;
            String sql = "UPDATE bebida_carbonatada SET nivel_gasificacion = ?, sabor = ? WHERE codigo_producto = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setDouble(1, bc.getNivelGasificacion());
                ps.setString(2, bc.getSabor());
                ps.setString(3, bc.getCodigo());
                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se actualizo la bebida carbonatada.");
                }
            }
        } else if (producto instanceof BebidaNatural) {
            BebidaNatural bn = (BebidaNatural) producto;
            String sql = "UPDATE bebida_natural SET porcentaje_fruta = ?, contiene_conservantes = ? WHERE codigo_producto = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setDouble(1, bn.getPorcentajeFruta());
                ps.setBoolean(2, bn.isContieneConservantes());
                ps.setString(3, bn.getCodigo());
                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se actualizo la bebida natural.");
                }
            }
        } else if (producto instanceof AguaEmbotellada) {
            AguaEmbotellada ae = (AguaEmbotellada) producto;
            String sql = "UPDATE agua_embotellada SET tipo_agua = ?, origen =? WHERE codigo_producto = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, ae.getTipoAgua());
                ps.setString(2, ae.getOrigen());
                ps.setString(3, ae.getCodigo());
                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se actualizo el agua embotellada.");
                }
            }
        }
    }

    private int obtenerIdTipo(Producto producto) throws  PersistenciaException{
        if(producto instanceof BebidaCarbonatada){
            return TIPO_CARBONATADA;
        }
        if (producto instanceof BebidaNatural){
            return TIPO_NATURAL;
        }
        if (producto instanceof AguaEmbotellada){
            return TIPO_AGUA;
        }
        throw new PersistenciaException("Tipo de producto desconocido.");
    }
    static Producto mapearProducto(ResultSet rs) throws SQLException{
        int idTipo = rs.getInt("id_tipo");
        String codigo = rs.getString("codigo");
        String nombre = rs.getString("nombre");
        double precioUnitario = rs.getDouble("precio_unitario");
        int stock = rs.getInt("stock");
        int volumenMl = rs.getInt("volumen_ml");

        if (idTipo == TIPO_CARBONATADA){
            return new BebidaCarbonatada(codigo,nombre,precioUnitario, stock,volumenMl,
                    rs.getDouble("nivel_gasificacion"), rs.getString("sabor"));
        } else if (idTipo == TIPO_NATURAL) {
            return new BebidaNatural(codigo,nombre,precioUnitario,stock,volumenMl,
                    rs.getDouble("porcentaje_fruta"), rs.getBoolean("contiene_conservantes"));
        } else if (idTipo == TIPO_AGUA){
            return new AguaEmbotellada(codigo,nombre,precioUnitario,stock,volumenMl,
                    rs.getString("tipo_agua"), rs.getString("origen"));
        }else{
            throw new SQLException("Tipo de producto desconocido en BD: " + idTipo);
        }
    }
}



