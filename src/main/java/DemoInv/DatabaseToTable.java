package DemoInv;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseToTable {

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nba",
                    "root",
                    "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void cargarDatos(JTable table) {
        Connection con = getConnection();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpiar la tabla

        String query = "select codigo, equipo_local from partidos";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Crear las columnas en la tabla
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }
            model.setColumnIdentifiers(columnNames);

            // Llenar filas de la tabla con los datos del ResultSet
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
