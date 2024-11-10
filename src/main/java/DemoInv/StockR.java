package DemoInv;

import javax.swing.*;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockR {

    // Método que inicia la revisión periódica de stock
    public static void iniciarRevisiónStock(VentanaPrincipal ventana) {
        // Programar la tarea de revisión de stock cada 10 segundos
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> revisarStock(ventana), 0, 10, TimeUnit.SECONDS);  // Ejecuta la revisión cada 10 segundos
    }

    // Método para revisar el stock y agregar productos a surtir
    public static void revisarStock(VentanaPrincipal ventana) {
        // Consulta SQL que une productos, stock y proveedores
        String consulta = "SELECT p.ID_Producto, p.Nombre, p.Descripcion, p.Categoria, p.Marca, "
                + "pr.ID_Proveedor, s.Cantidad, s.Limite, pr.NombreProveedor, pr.Telefono, pr.Email "
                + "FROM productos p "
                + "JOIN stock s ON p.ID_Producto = s.ID_Producto "
                + "JOIN proveedores pr ON p.ID_Proveedor = pr.ID_Proveedor "
                + "WHERE s.Cantidad < s.Limite";

        try (Connection conn = DatabaseToTable.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            boolean productosBajos = false;  // Bandera para saber si hay productos bajos

            // Recorremos los resultados de la consulta
            while (rs.next()) {
                // Consulta para verificar si el producto ya está en la tabla productos_a_surtir
                String verificarExistencia = "SELECT COUNT(*) FROM productos_a_surtir WHERE ID_Producto = ?";
                try (PreparedStatement psVerificar = conn.prepareStatement(verificarExistencia)) {
                    psVerificar.setInt(1, rs.getInt("ID_Producto"));
                    try (ResultSet rsVerificar = psVerificar.executeQuery()) {
                        if (rsVerificar.next() && rsVerificar.getInt(1) == 0) {
                            // Si el producto no existe, realizar la inserción
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
                                psInsertar.executeUpdate();  // Ejecutar la inserción
                                productosBajos = true;  // Si encontramos productos bajos, cambiar la bandera

                                String mensaje = "¡Producto bajo de stock! Producto: " + rs.getString("Nombre")
                                        + ", Proveedor: " + rs.getString("NombreProveedor")
                                        + ", Teléfono: " + rs.getString("Telefono");
                                TwilioSMS.enviarMensaje(mensaje);  // Método que envía el mensaje a través de Twilio
                            }
                        }
                    }
                }
            }

            // Si hay productos a surtir, mostrar una notificación en la ventana principal
            if (productosBajos) {
                SwingUtilities.invokeLater(() -> {
                    ventana.mostrarNotificacion("¡Productos a surtir detectados!");
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
