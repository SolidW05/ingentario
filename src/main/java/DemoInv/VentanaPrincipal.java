package DemoInv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VentanaPrincipal extends JFrame {
    private JPanel VentanaPrincipal;
    private JPanel panelBotones;
    private JButton btnStock;
    private JButton btnProvedores;
    private JButton btnTrabajadores;
    private JPanel paneles;
    private JPanel panelProductos;
    private JPanel panelTrabajadores;
    private JPanel panelStock;
    private JButton btnProductos;
    private JLabel Titulo;
    private JPanel panelProvedores;
    private JButton Editar;
    private JButton Buscar;
    private JButton Nuevo;
    private JButton Eliminar;
    private JPanel OpcionBotones;
    private JButton productosButton;
    private JButton sucursalButton;
    private JButton trabajadoresButton;

    public VentanaPrincipal() {

        add(VentanaPrincipal);
        pack();

        paneles.add(panelTrabajadores, "panelTrabajadores");
        paneles.add(panelProvedores, "panelProvedores");
        paneles.add(panelProductos, "panelProductos");
        paneles.add(panelStock, "panelStock");

        // agregar tablas
        // Productos
        DefaultTableModel mdlProductos = new DefaultTableModel();
        DatabaseToTable.cargarDatos(mdlProductos, "select * from productos");
        JTable tblProductos = new JTable(mdlProductos);
        JScrollPane scrlproductos = new JScrollPane(tblProductos);
        panelProductos.add(scrlproductos);
        // Stock

        DefaultTableModel mdlStock = new DefaultTableModel();
        DatabaseToTable.cargarDatos(mdlStock, "select * from stock");
        JTable tblStock = new JTable(mdlStock);
        JScrollPane scrlStock = new JScrollPane(tblStock);
        panelStock.add(scrlStock);

        //Provedores

        DefaultTableModel mdlProvedores = new DefaultTableModel();
        DatabaseToTable.cargarDatos(mdlProvedores, "select * from proveedores");
        JTable tblProveedores = new JTable(mdlProvedores);
        JScrollPane scrlProveedores = new JScrollPane(tblProveedores);
        panelProvedores.add(scrlProveedores);

        //Trabajadores

        DefaultTableModel mdlTrabajadores = new DefaultTableModel();
        DatabaseToTable.cargarDatos(mdlTrabajadores, "select nombre, puesto, telefono,email from trabajador");
        JTable tblTrabajadores = new JTable(mdlTrabajadores);
        JScrollPane scrlTrabajadores = new JScrollPane(tblTrabajadores);
        panelTrabajadores.add(scrlTrabajadores);

        // Configurar botones
        configurarBotones(btnTrabajadores);
        configurarBotones(btnProvedores);
        configurarBotones(btnProductos);
        configurarBotones(btnStock);

        // Agregar imágenes a los botones
        agregarImagenABoton(btnProductos, "src/ImagenesButton/Productos.png");
        agregarImagenABoton(btnStock, "src/ImagenesButton/Sucursal.png");
        agregarImagenABoton(btnProvedores, "src/ImagenesButton/Provedores.png");
        agregarImagenABoton(btnTrabajadores, "src/ImagenesButton/Trabajadores.png");

        btnStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelStock");
            }
        });
        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelProductos");
            }
        });
        btnTrabajadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelTrabajadores");
            }
        });

        btnProvedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelProvedores");
            }
        });

        setVisible(true);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelBuscar");
            }
        });


        Buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String terminoBusqueda = JOptionPane.showInputDialog(null, "Ingrese el nombre del producto a buscar:");

                if (terminoBusqueda != null && !terminoBusqueda.trim().isEmpty()) {
                    boolean encontrado = false;

                    // Itera sobre las filas de la tabla de productos para buscar coincidencias
                    for (int i = 0; i < mdlProductos.getRowCount(); i++) {
                        Object nombreProductoObj = mdlProductos.getValueAt(i, 1); // Cambia el índice según la columna
                        String nombreProducto = nombreProductoObj != null ? nombreProductoObj.toString() : "";

                        if (nombreProducto.toLowerCase().contains(terminoBusqueda.toLowerCase())) {
                            tblProductos.setRowSelectionInterval(i, i);
                            tblProductos.scrollRectToVisible(tblProductos.getCellRect(i, 0, true));

                            // Esta es la estructura segun como se va a mostrar la info del producto
                            StringBuilder detallesProducto = new StringBuilder("Detalles del producto:\n");
                            for (int j = 0; j < mdlProductos.getColumnCount(); j++) {
                                String nombreColumna = mdlProductos.getColumnName(j);
                                Object valorCeldaObj = mdlProductos.getValueAt(i, j);
                                String valorCelda = valorCeldaObj != null ? valorCeldaObj.toString() : "N/A";
                                detallesProducto.append(nombreColumna).append(": ").append(valorCelda).append("\n");
                            }
                            JOptionPane.showMessageDialog(null, detallesProducto.toString());

                            encontrado = true;
                            break;
                        }
                    }

                    // Esto solo en caso de que no exista el producto
                    if (!encontrado) {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un término de búsqueda.");
                }
            }

        });
        Nuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panelNuevoProducto = new JPanel(new GridLayout(0, 2, 5, 5));

                // Campos para la entrada de un nuevo registro
                JTextField txtNombre = new JTextField();
                JTextField txtDescripcion = new JTextField();
                JTextField txtCategoria = new JTextField();
                JTextField txtMarca = new JTextField();
                JTextField txtProveedor = new JTextField(); // Para ID_Proveedor
                JTextField txtPrecio = new JTextField();

                // Agregar etiquetas y campos de texto al panel
                panelNuevoProducto.add(new JLabel("Nombre:"));
                panelNuevoProducto.add(txtNombre);
                panelNuevoProducto.add(new JLabel("Descripción:"));
                panelNuevoProducto.add(txtDescripcion);
                panelNuevoProducto.add(new JLabel("Categoría:"));
                panelNuevoProducto.add(txtCategoria);
                panelNuevoProducto.add(new JLabel("Marca:"));
                panelNuevoProducto.add(txtMarca);
                panelNuevoProducto.add(new JLabel("ID Proveedor:"));
                panelNuevoProducto.add(txtProveedor);
                panelNuevoProducto.add(new JLabel("Precio:"));
                panelNuevoProducto.add(txtPrecio);

                // Muestra el mensaje(osea la otra pantalla)
                int result = JOptionPane.showConfirmDialog(null, panelNuevoProducto, "Agregar Nuevo Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String nombre = txtNombre.getText().trim();
                    String descripcion = txtDescripcion.getText().trim();
                    String categoria = txtCategoria.getText().trim();
                    String marca = txtMarca.getText().trim();
                    String proveedorStr = txtProveedor.getText().trim();
                    String precioStr = txtPrecio.getText().trim();

                    if (nombre.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || marca.isEmpty() || proveedorStr.isEmpty() || precioStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                        return;
                    }

                    try {
                        double precio = Double.parseDouble(precioStr);
                        int idProveedor = Integer.parseInt(proveedorStr);

                        // Aqui se hace la consulta y se aplica el registro
                        String sql = "INSERT INTO productos (Nombre, Descripcion, Categoria, Marca, ID_Proveedor, Precio) VALUES (?, ?, ?, ?, ?, ?)";
                        try (Connection con = DatabaseToTable.getConnection();
                             PreparedStatement stmt = con.prepareStatement(sql)) {
                            stmt.setString(1, nombre);
                            stmt.setString(2, descripcion);
                            stmt.setString(3, categoria);
                            stmt.setString(4, marca);
                            stmt.setInt(5, idProveedor);
                            stmt.setDouble(6, precio);
                            stmt.executeUpdate();
                        }

                        // Actualizar el DefaultTableModel
                        mdlProductos.addRow(new Object[]{nombre, descripcion, categoria, marca, idProveedor, precio});

                        JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Precio y ID Proveedor deben ser valores numéricos.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al agregar el producto: " + ex.getMessage());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage());
                    }
                }
            }
        });


        Eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solicita que quieres eliminar
                String criterio = JOptionPane.showInputDialog("Ingrese el ID o el Nombre del producto a eliminar:");

                // Validar la entrada
                if (criterio != null && !criterio.trim().isEmpty()) {
                    String sql;
                    boolean esId = false;

                    // Verificar que si sea un id
                    try {
                        int idProducto = Integer.parseInt(criterio.trim());
                        esId = true;
                        sql = "DELETE FROM productos WHERE ID_Producto = ?";
                    } catch (NumberFormatException ex) {
                        esId = false;
                        sql = "DELETE FROM productos WHERE Nombre = ?";
                    }

                    // Conectar a la base de datos y eliminar
                    try (Connection con = DatabaseToTable.getConnection();
                         PreparedStatement stmt = con.prepareStatement(sql)) {
                        stmt.setString(1, criterio.trim()); // Usar el criterio ingresado

                        int filasEliminadas = stmt.executeUpdate();

                        if (filasEliminadas > 0) {
                            // Actualizar el DefaultTableModel
                            for (int i = 0; i < mdlProductos.getRowCount(); i++) {
                                if (esId && mdlProductos.getValueAt(i, 0).equals(Integer.parseInt(criterio.trim()))) {
                                    mdlProductos.removeRow(i);
                                    break;
                                } else if (!esId && mdlProductos.getValueAt(i, 1).equals(criterio.trim())) { // Suponiendo que el nombre es la segunda columna
                                    mdlProductos.removeRow(i);
                                    break;
                                }
                            }
                            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró ningún producto con el criterio especificado.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un criterio válido.");
                }
            }
        });

    }



    private void configurarBotones(JButton boton) {
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setBackground(Color.BLACK);
        boton.setForeground(Color.WHITE);
    }

    private void agregarImagenABoton(JButton boton, String rutaImagen) {
        ImageIcon icono = new ImageIcon(rutaImagen);
        boton.setIcon(icono);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
