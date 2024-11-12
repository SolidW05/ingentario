package DemoInv;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;

import static javax.swing.JOptionPane.*;

public class InsertToRecepcion extends DatabaseToTable {

    public static Object[] cargarDatos(JTextField[] campos) {
        Object[] valuesObj = new Object[campos.length + 1];
        valuesObj[0] = campos[0].getText();
        valuesObj[1] = LocalDate.now();
        for (int i = 1; i < campos.length; i++) {
            String valor = campos[i].getText();
            try {
                valuesObj[i+1] = Integer.parseInt(valor);
            } catch (NumberFormatException e1) {
                try {
                    valuesObj[i+1] = Double.parseDouble(valor);
                } catch (NumberFormatException e2) {
                    valuesObj[i+1] = valor;
                }
            }
        }
        String placeholders = String.join(", ",
                java.util.Collections.nCopies(valuesObj.length, "?"));

        String sql = "INSERT INTO recepcion (ID_Trabajador, Fecha_Recepcion, Cantidad, Costo, ID_Stock) VALUES\n" +
                "(" + placeholders + ")";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < valuesObj.length; i++) {
                if (valuesObj[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) valuesObj[i]);
                } else if (valuesObj[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) valuesObj[i]);
                } else {
                    pstmt.setString(i + 1, valuesObj[i].toString());
                }
            }

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto ingresado",
                    "Error", JOptionPane.ERROR_MESSAGE);
            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);

                    // Crear un nuevo array que incluya el ID y los valores
                    Object[] resultado = new Object[valuesObj.length + 1];
                    resultado[0] = generatedId; // Agregar el ID autogenerado en la primera posición
                    System.arraycopy(valuesObj, 0, resultado, 1, valuesObj.length); // Copiar los valores restantes
                    return resultado;
                } else {
                    throw new SQLException("No se pudo obtener el ID autogenerado.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al añadir producto", "Error", ERROR_MESSAGE);
            return null;
        }
    }
}
