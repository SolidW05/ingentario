package DemoInv;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseToTable {
    public static Thread hilo;
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventario",
                    "root",
                    "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void cargarDatos(DefaultTableModel model, String query) {
        Connection con = getConnection();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            model.setRowCount(0);
            // Crear las columnas en la tabla
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }
            model.setColumnIdentifiers(columnNames);  // CORRECCIÓN AQUÍ

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
    public static void ActualizaTabla(DefaultTableModel model, String query) {
        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt();
        }

        hilo = new Thread(() -> {
            while (true) {
                try {
                    cargarDatos(model, query);

                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        hilo.setDaemon(true); // Termina cuando la aplicación finaliza
        hilo.start(); // Iniciar el hilo
    }
}
