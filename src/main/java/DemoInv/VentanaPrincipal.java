package DemoInv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JPanel panelBuscar;
    private JTextField JTBuscarP;
    private JButton BTBuscar;
    private JTable table1;
    private JButton productosButton;
    private JButton sucursalButton;
    private JButton trabajadoresButton;

    public VentanaPrincipal() {

        add(VentanaPrincipal);
        pack();

        paneles.add(panelTrabajadores, "panelTrabajadores" );
        paneles.add(panelProvedores, "panelProvedores" );
        paneles.add(panelProductos, "panelProductos" );
        paneles.add(panelStock, "panelStock" );
        paneles.add(panelBuscar, "panelBuscar");

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

        BTBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String terminoBusqueda = JTBuscarP.getText().trim();

                if (!terminoBusqueda.isEmpty()) {
                    boolean encontrado = false;

                    for (int i = 0; i < mdlProductos.getRowCount(); i++) {
                        Object nombreProductoObj = mdlProductos.getValueAt(i, 1);  // va cambiandp entre indices segun la columna
                        String nombreProducto = nombreProductoObj != null ? nombreProductoObj.toString() : "";

                        if (nombreProducto.toLowerCase().contains(terminoBusqueda.toLowerCase())) {
                            tblProductos.setRowSelectionInterval(i, i);
                            tblProductos.scrollRectToVisible(tblProductos.getCellRect(i, 0, true));

                            // Construye la información del producto
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

                    if (!encontrado) {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un término de búsqueda.");
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
