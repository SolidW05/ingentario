package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {
    private JPanel VentanaPrincipal;
    private JPanel panelBotones;
    private JButton button1;
    private JButton almacenButton;
    private JButton ETCButton;
    private JPanel paneles;
    private JButton tablaStock;
    private JPanel panelProductos;
    private JPanel panelTrabajadores;
    private JPanel panelStock;
    private JButton provedoresButton;
    private JLabel Titulo;
    private JButton productosButton;
    private JButton sucursalButton;
    private JButton trabajadoresButton;

    public VentanaPrincipal() {
        // Inicializar los botones manualmente
        productosButton = new JButton("Productos");
        sucursalButton = new JButton("Sucursal");
        provedoresButton = new JButton("Proveedores");
        trabajadoresButton = new JButton("Trabajadores");

        // Agregar los botones al panel de botones
        panelBotones = new JPanel(new GridLayout(4, 1));
        panelBotones.add(productosButton);
        panelBotones.add(sucursalButton);
        panelBotones.add(provedoresButton);
        panelBotones.add(trabajadoresButton);

        // Configurar el layout de la ventana
        setLayout(new BorderLayout());
        add(panelBotones, BorderLayout.WEST);
        add(paneles, BorderLayout.CENTER);

        // Configurar botones
        configurarBotones(productosButton);
        configurarBotones(sucursalButton);
        configurarBotones(provedoresButton);
        configurarBotones(trabajadoresButton);

        // Agregar imágenes a los botones
        agregarImagenABoton(productosButton, "src/ImagenesButton/Productos.png");
        agregarImagenABoton(sucursalButton, "src/ImagenesButton/Sucursal.png");
        agregarImagenABoton(provedoresButton, "src/ImagenesButton/Provedores.png");
        agregarImagenABoton(trabajadoresButton, "src/ImagenesButton/Trabajadores.png");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelStock");
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

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
