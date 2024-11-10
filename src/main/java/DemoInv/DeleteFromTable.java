package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class DeleteFromTable
{
    private static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventario",
                    "root",
                    "1234");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }

    public static boolean delete(String nombreTabla, String nomColum, int id){
        String sql = "DELETE FROM "+ nombreTabla + " WHERE " + nomColum + " = ?";

        try(Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int columnasAfectadas = pstmt.executeUpdate();
            if (columnasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Fila Eliminada Con exito",
                        "Eliminado", JOptionPane.PLAIN_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el registro en la base de datos.",
                        "Eliminado", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro de la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}