package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditFromTable {

    public static Object[] update(String nombreTabla, int id, String nameID){


        String[] columnas = getColumnName(nombreTabla);
        Object[] prevValues = getPrevValues(nombreTabla, nameID, id);
        String[] valores = getValues(columnas, prevValues);

        if (valores == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return null;
        }
        Object[] valoresObj = toObject(valores);
        try {
            UpdateRow(nombreTabla, columnas, valoresObj, nameID, id);
            JOptionPane.showMessageDialog(null, "Fila actualizada Correctamente " + nombreTabla);
            return valoresObj;
        }catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar datos en " + nombreTabla,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    private static void UpdateRow(String nombretabla, String[] nomCol, Object[] values, String IDname, int id) throws SQLException {

        int length = nomCol.length - 1;
        String[] cols = new String[length];
        System.arraycopy(nomCol, 1, cols, 0, length);

        int valuesLength = values.length - 1;
        Object[] vals = new Object[valuesLength];
        System.arraycopy(values, 1, vals, 0, valuesLength);


        StringBuilder sql = new StringBuilder("UPDATE " + nombretabla + " SET ");
        for (int i = 0; i < cols.length; i++) {
            sql.append(cols[i]).append(" = ?");
            if(i < cols.length - 1) sql.append(",");
        }
        sql.append(" WHERE " + IDname + " = " + id);

        try(Connection con = DatabaseToTable.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < vals.length; i++) {
                if (vals[i] instanceof Integer) {
                    pstmt.setInt(i + 1 , (Integer) vals[i]);
                } else if (vals[i] instanceof Double) {
                    pstmt.setDouble(i + 1 , (Double) vals[i]);
                } else {
                    pstmt.setString(i +  1 , vals[i].toString());
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

    private static String[] getValues(String[] columnas, Object[] prevValues){
        JPanel panelNuevoRegistro = new JPanel(new GridLayout(0, 2,
                5, 5));

        JTextField[] campos = new JTextField[columnas.length];
        for (int i = 0; i < campos.length; i++) {
            campos[i] = new JTextField(20);
            String prevValue = prevValues[i] != null ? prevValues[i].toString() : "";
            campos[i].setText(prevValue);
            if (!columnas[i].equalsIgnoreCase("Cantidad")){
            panelNuevoRegistro.add(new JLabel(columnas[i] +":"));
            panelNuevoRegistro.add(campos[i]);
            }
        }
        campos[0].setEditable(false);
        int result = JOptionPane.showConfirmDialog(null, panelNuevoRegistro,
                "Editar datos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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
    private static String[] getColumnName(String tableName) {
        String query = "SELECT * FROM " + tableName + " LIMIT 1";
        try (Connection con = DatabaseToTable.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Crear las columnas en la tabla
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount ; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }
            return columnNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object[] getPrevValues(String tableName,String nameID, int id){
        String query = "SELECT * FROM " + tableName + " WHERE " + nameID + " = " + id;
        try (Connection con = DatabaseToTable.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int columnas = rs.getMetaData().getColumnCount();
            Object[] row = new Object[columnas];
            while (rs.next()) {
                for (int i = 1; i <= columnas; i++) {
                    row[i - 1] = rs.getObject(i);
                }
            }

            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {

        update("Productos", 1, "ID_PRODUCTO");
    }

}
