package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InsertToTable {

    public static void insert(String nombreTabla){

        String[] columnas = getColumnNames(nombreTabla);
        String[] valores = getValues(columnas);
        if (valores == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return;
        }
        Object[] valoresobj = toObject(valores);

        try {
            insertRow(nombreTabla, columnas, valoresobj);
            JOptionPane.showMessageDialog(null, "Datos insertados exitosamente en " + nombreTabla);
        }catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar datos en " + nombreTabla,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private static void insertRow(String nombretabla, String[] nomCol, Object[] values) throws SQLException {
        String columns = String.join(", ", nomCol);
        String placeholders = String.join(", ", java.util.Collections.nCopies(nomCol.length, "?"));


        String sql = "INSERT INTO " + nombretabla + " (" + columns + ") VALUES (" + placeholders + ")";

        try(Connection con = DatabaseToTable.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }
            pstmt.executeUpdate();
        }

    }


    private static Object[] toObject(String[] values){
        Object[] valuesObj = new Object[values.length];
        for(int i = 0; i < values.length; i++){
            String valor = values[i];
            try{
                valuesObj[i] = Integer.parseInt(valor);
            }catch (NumberFormatException e1){
                try {
                    valuesObj[i] = Double.parseDouble(valor);
                }catch (NumberFormatException e2){

                    valuesObj[i] = valor;
                }
            }
        }
        return valuesObj;
    }

    private static String[] getValues(String[] columnas){
        JPanel panelNuevoRegistro = new JPanel(new GridLayout(0, 2,
                5, 5));

        JTextField[] campos = new JTextField[columnas.length];
        for (int i = 0; i < campos.length; i++) {
            campos[i] = new JTextField(20);
            panelNuevoRegistro.add(new JLabel(columnas[i] +":"));
            panelNuevoRegistro.add(campos[i]);
        }
        int result = JOptionPane.showConfirmDialog(null, panelNuevoRegistro,
                "Insertar datos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        for (int i = 0; i < campos.length; i++) {
            if (campos[i].getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return null;
            }
        }

        if (result == JOptionPane.OK_OPTION) {
            String[] valores = new String[columnas.length];
            for (int i = 0; i < campos.length; i++) {
                valores[i] = campos[i].getText().trim();
            }
            return valores;
        } else {
            return null;
        }

    }
    private static String[] getColumnNames(String tableName) {
        String query = "SELECT * FROM " + tableName + " LIMIT 1";
        try (Connection con = DatabaseToTable.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Crear las columnas en la tabla
            String[] columnNames = new String[columnCount - 1];
            for (int i = 2; i <= columnCount ; i++) {
                columnNames[i - 2] = rsmd.getColumnName(i);
            }
            return columnNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // ESte solo sirve para probar
    public static void main(String[] args) {
        insert("Productos");
        }
}


