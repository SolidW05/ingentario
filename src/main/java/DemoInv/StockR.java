// Importaciones necesarias
package DemoInv;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockR {

    // Credenciales de Twilio
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    // Método para iniciar revisión de stock periódica
    public static void iniciarRevisiónStock() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> revisarStock(), 0, 10, TimeUnit.SECONDS);
    }

    public static void revisarStock() {
        String consulta = "SELECT p.ID_Producto, p.Nombre, p.Descripcion, p.Categoria, p.Marca, "
                + "pr.ID_Proveedor, s.Cantidad, s.Limite, pr.NombreProveedor, pr.Telefono, pr.Email "
                + "FROM productos p "
                + "JOIN stock s ON p.ID_Producto = s.ID_Producto "
                + "JOIN proveedores pr ON p.ID_Proveedor = pr.ID_Proveedor "
                + "WHERE s.Cantidad < s.Limite";

        try (Connection conn = DatabaseToTable.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            while (rs.next()) {
                String verificarExistencia = "SELECT COUNT(*) FROM productos_a_surtir WHERE ID_Producto = ?";
                try (PreparedStatement psVerificar = conn.prepareStatement(verificarExistencia)) {
                    psVerificar.setInt(1, rs.getInt("ID_Producto"));
                    try (ResultSet rsVerificar = psVerificar.executeQuery()) {
                        if (rsVerificar.next() && rsVerificar.getInt(1) == 0) {
                            String insertar = "INSERT INTO productos_a_surtir (ID_Producto, Nombre, Descripcion, Categoria, Marca, "
                                    + "ID_Proveedor, Cantidad, Limite, NombreProveedor, TelefonoProveedor, EmailProveedor) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement psInsertar = conn.prepareStatement(insertar)) {
                                psInsertar.setInt(1, rs.getInt("ID_Producto"));
                                psInsertar.setString(2, rs.getString("Nombre"));
                                psInsertar.setString(3, rs.getString("Descripcion"));
                                psInsertar.setString(4, rs.getString("Categoria"));
                                psInsertar.setString(5, rs.getString("Marca"));
                                psInsertar.setInt(6, rs.getInt("ID_Proveedor"));
                                psInsertar.setInt(7, rs.getInt("Cantidad"));
                                psInsertar.setInt(8, rs.getInt("Limite"));
                                psInsertar.setString(9, rs.getString("NombreProveedor"));
                                psInsertar.setString(10, rs.getString("Telefono"));
                                psInsertar.setString(11, rs.getString("Email"));
                                psInsertar.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarProductosASurtir() {
        String consulta = "SELECT ID_Producto, Nombre, Descripcion, Categoria, Marca, ID_Proveedor, "
                + "Cantidad, Limite, NombreProveedor, TelefonoProveedor, EmailProveedor FROM productos_a_surtir";

        try (Connection conn = DatabaseToTable.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            Vector<String> columnas = new Vector<>();
            columnas.add("ID Producto");
            columnas.add("Nombre");
            columnas.add("Descripcion");
            columnas.add("Cantidad");
            columnas.add("Limite");
            columnas.add("Proveedor");

            Vector<Vector<Object>> datos = new Vector<>();

            while (rs.next()) {
                Vector<Object> fila = new Vector<>();
                fila.add(rs.getInt("ID_Producto"));
                fila.add(rs.getString("Nombre"));
                fila.add(rs.getString("Descripcion"));
                fila.add(rs.getInt("Cantidad"));
                fila.add(rs.getInt("Limite"));
                fila.add(rs.getString("NombreProveedor"));
                datos.add(fila);
            }

            DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
            JTable tabla = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tabla);

            // Crear botón "Pedir"
            JButton btnPedir = new JButton("Pedir");
            btnPedir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int filaSeleccionada = tabla.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        int idProducto = (int) tabla.getValueAt(filaSeleccionada, 0);
                        String nombreProducto = (String) tabla.getValueAt(filaSeleccionada, 1);

                        String cantidad = JOptionPane.showInputDialog(
                                null,
                                "¿Cuántos de " + nombreProducto + " deseas surtir?"
                        );

                        if (cantidad != null && !cantidad.isEmpty()) {
                            enviarMensaje(idProducto, nombreProducto, cantidad);
                            eliminarProductoASurtir(idProducto);
                            ((DefaultTableModel) tabla.getModel()).removeRow(filaSeleccionada);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione un producto.");
                    }
                }
            });

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(btnPedir, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(null, panel, "Productos a Surtir", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void enviarMensaje(int idProducto, String nombreProducto, String cantidad) {
        String mensaje = "Pedido del producto " + nombreProducto + " por cantidad de " + cantidad + ".";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+523329266146"),
                        new com.twilio.type.PhoneNumber("+14425001326"),
                        mensaje)
                .create();
        JOptionPane.showMessageDialog(null, "Mensaje enviado: " + message.getBody(), "Mensaje Enviado", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void eliminarProductoASurtir(int idProducto) {
        String eliminar = "DELETE FROM productos_a_surtir WHERE ID_Producto = ?";
        try (Connection conn = DatabaseToTable.getConnection();
             PreparedStatement ps = conn.prepareStatement(eliminar)) {
            ps.setInt(1, idProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
