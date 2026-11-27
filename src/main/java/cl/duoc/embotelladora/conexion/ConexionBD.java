package cl.duoc.embotelladora.conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/embotelladora";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";
    private ConexionBD() {
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}