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
    public VentanaPrincipal() {

        add(VentanaPrincipal);
        pack();

        paneles.add(panelTrabajadores, "panelTrabajadores" );
        paneles.add(panelProvedores, "panelProvedores" );
        paneles.add(panelProductos, "panelProductos" );
        paneles.add(panelStock, "panelStock" );

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

    public static void main(String[] args) { new VentanaPrincipal();
    }
}
