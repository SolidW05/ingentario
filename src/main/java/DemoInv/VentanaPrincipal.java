package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame{
    private JPanel panel1;
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

    public VentanaPrincipal(){
        add(panel1);
        pack();

        paneles.add(panelTrabajadores, "panelTrabajadores");
        paneles.add(panelProductos, "panelProductos");
        paneles.add(panelStock, "panelStock");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelStock");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
