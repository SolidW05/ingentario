package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InsertToTable {

    public static Object[] insert(String nombreTabla){
        String[] columnas = getColumnNames(nombreTabla);
        String[] valores = getValues(columnas);
        if (valores == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return null;
        }
        Object[] valoresobj = toObject(valores);

        try {
            Object[] resultado = insertRow(nombreTabla, columnas, valoresobj);
            JOptionPane.showMessageDialog(null, "Datos insertados exitosamente en " + nombreTabla);
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar datos en " + nombreTabla,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static Object[] insertRow(String nombretabla, String[] nomCol, Object[] values) throws SQLException {
        String columns = String.join(", ", nomCol);
        String placeholders = String.join(", ", java.util.Collections.nCopies(nomCol.length, "?"));

        String sql = "INSERT INTO " + nombretabla + " (" + columns + ") VALUES (" + placeholders + ")";

        try (Connection con = DatabaseToTable.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);

                    // Crear un nuevo array que incluya el ID y los valores
                    Object[] resultado = new Object[values.length + 1];
                    resultado[0] = generatedId; // Agregar el ID autogenerado en la primera posición
                    System.arraycopy(values, 0, resultado, 1, values.length); // Copiar los valores restantes
                    return resultado;
                } else {
                    throw new SQLException("No se pudo obtener el ID autogenerado.");
                }
            }
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
        JPanel panelNuevoRegistro = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField[] campos = new JTextField[columnas.length];
        for (int i = 0; i < campos.length; i++) {
            campos[i] = new JTextField(20);
            panelNuevoRegistro.add(new JLabel(columnas[i] + ":"));
            panelNuevoRegistro.add(campos[i]);
        }
        int result = JOptionPane.showConfirmDialog(null, panelNuevoRegistro,
                "Insertar datos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


        if (result == JOptionPane.OK_OPTION) {
            String[] valores = new String[columnas.length];
            for (int i = 0; i < campos.length; i++) {
                if (campos[i].getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                    return null;
                }
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

            String[] columnNames = new String[columnCount - 1];
            for (int i = 2; i <= columnCount; i++) {
                columnNames[i - 2] = rsmd.getColumnName(i);
            }
            return columnNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método de prueba
    public static void main(String[] args) {

        insert("Trabajador");
    }
}

