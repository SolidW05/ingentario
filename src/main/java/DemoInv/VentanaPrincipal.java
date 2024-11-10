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

    private  DefaultTableModel mdlProductos, mdlStock,
            mdlProvedores, mdlTrabajadores;

    private JTable tblTrabajadores, tblProveedores, tblStock, tblProductos;


    private JTable d;

    public VentanaPrincipal() {

        add(VentanaPrincipal);
        pack();

        paneles.add(panelProductos, "panelProductos");
        paneles.add(panelTrabajadores, "panelTrabajadores");
        paneles.add(panelProvedores, "panelProvedores");
        paneles.add(panelStock, "panelStock");

        // agregar tablas
        // Productos
        mdlProductos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DatabaseToTable.cargarDatos(mdlProductos, "select * from productos");
        tblProductos = new JTable(mdlProductos);
        JScrollPane scrlproductos = new JScrollPane(tblProductos);
        panelProductos.add(scrlproductos);
        // Stock

        mdlStock = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DatabaseToTable.cargarDatos(mdlStock, "select * from stock");
        tblStock = new JTable(mdlStock);
        JScrollPane scrlStock = new JScrollPane(tblStock);
        panelStock.add(scrlStock);

        //Provedores
        mdlProvedores = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DatabaseToTable.cargarDatos(mdlProvedores, "select * from proveedores");
        tblProveedores = new JTable(mdlProvedores);
        JScrollPane scrlProveedores = new JScrollPane(tblProveedores);
        panelProvedores.add(scrlProveedores);

        //Trabajadores

        mdlTrabajadores = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DatabaseToTable.cargarDatos(mdlTrabajadores, "select ID_Trabajador, Nombre, Puesto ,Telefono ,Email ,Direccion from trabajador");
        tblTrabajadores = new JTable(mdlTrabajadores);
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
                panelStock.add(OpcionBotones, BorderLayout.SOUTH);
                Editar.setEnabled(false);
                Eliminar.setEnabled(false);
            }
        });
        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelProductos");
                panelProductos.add(OpcionBotones, BorderLayout.SOUTH);
                Editar.setEnabled(false);
                Eliminar.setEnabled(false);
            }
        });
        btnTrabajadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelTrabajadores");
                panelTrabajadores.add(OpcionBotones, BorderLayout.SOUTH);
                Editar.setEnabled(false);
                Eliminar.setEnabled(false);
            }
        });

        btnProvedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (paneles.getLayout());
                cl.show(paneles, "panelProvedores");
                panelProvedores.add(OpcionBotones, BorderLayout.SOUTH);
                Editar.setEnabled(false);
                Eliminar.setEnabled(false);
            }
        });

        Buscar.addActionListener(e -> System.out.println());
        Nuevo.addActionListener(e -> insert());

        Eliminar.addActionListener(e -> delete());

        Editar.addActionListener(e -> update());

        Editar.setEnabled(false);
        Eliminar.setEnabled(false);

        AddTableSelectionListener();

        setVisible(true);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void insert(){
            Object[] nuevoRegistro;
            if (panelProductos.isVisible()){
                nuevoRegistro = InsertToTable.insert("Productos");
                if (nuevoRegistro != null){
                    mdlProductos.addRow(nuevoRegistro);
                    mdlProductos.fireTableDataChanged();
                }
            } else if (panelProvedores.isVisible()) {
                nuevoRegistro = InsertToTable.insert("Proveedores");
                if (nuevoRegistro != null){
                    mdlProvedores.addRow(nuevoRegistro);
                    mdlProvedores.fireTableDataChanged();
                }

            } else if (panelTrabajadores.isVisible()) {
                nuevoRegistro = InsertToTable.insert("Trabajador");
                if (nuevoRegistro != null){
                    mdlTrabajadores.addRow(nuevoRegistro);
                    mdlTrabajadores.fireTableDataChanged();
                }
            } else if (panelStock.isVisible()) {
                nuevoRegistro = InsertToTable.insert("Stock");
                if (nuevoRegistro != null){
                    mdlStock.addRow(nuevoRegistro);
                    mdlStock.fireTableDataChanged();
                }
            }

    }

    private void update() {
        int filaSeleccionada;
        Object[] filaModificada;

        // Actualización en productos
        if (panelProductos.isVisible()) {
            filaSeleccionada = tblProductos.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblProductos.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblProductos.getColumnName(0);
                String nombreTabla = "productos";
                filaModificada = EditFromTable.update(nombreTabla, id, nombreColumna);  // Asegúrate que update devuelva Object[]
                if (filaModificada != null) {
                    for (int i = 0; i < filaModificada.length; i++) {  // Corregí el ciclo 'for'
                        mdlProductos.setValueAt(filaModificada[i], filaSeleccionada, i);
                    }
                    mdlProductos.fireTableDataChanged();  // Notificar a la tabla que se actualizó
                }
            }
        }
        // Actualización en proveedores
        else if (panelProvedores.isVisible()) {
            filaSeleccionada = tblProveedores.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblProveedores.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblProveedores.getColumnName(0);
                String nombreTabla = "proveedores";
                filaModificada = EditFromTable.update(nombreTabla, id, nombreColumna);  // Cambio a update
                if (filaModificada != null) {
                    for (int i = 0; i < filaModificada.length; i++) {  // Corregir el ciclo
                        mdlProvedores.setValueAt(filaModificada[i], filaSeleccionada, i);
                    }
                    mdlProvedores.fireTableDataChanged();  // Notificar que se actualizó
                }
            }
        }
        // Actualización en trabajadores
        else if (panelTrabajadores.isVisible()) {
            filaSeleccionada = tblTrabajadores.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblTrabajadores.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblTrabajadores.getColumnName(0);
                String nombreTabla = "Trabajador";
                filaModificada = EditFromTable.update(nombreTabla, id, nombreColumna);  // Cambio a update
                if (filaModificada != null) {
                    for (int i = 0; i < filaModificada.length; i++) {  // Corregir el ciclo
                        mdlTrabajadores.setValueAt(filaModificada[i], filaSeleccionada, i);
                    }
                    mdlTrabajadores.fireTableDataChanged();  // Notificar que se actualizó
                }
            }
        }
        // Actualización en stock
        else if (panelStock.isVisible()) {
            filaSeleccionada = tblStock.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblStock.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblStock.getColumnName(0);
                String nombreTabla = "Stock";
                filaModificada = EditFromTable.update(nombreTabla, id, nombreColumna);  // Cambio a update
                if (filaModificada != null) {
                    for (int i = 0; i < filaModificada.length; i++) {  // Corregir el ciclo
                        mdlStock.setValueAt(filaModificada[i], filaSeleccionada, i);
                    }
                    mdlStock.fireTableDataChanged();  // Notificar que se actualizó
                }
            }
        }
    }

    private void delete() {
        int filaSeleccionada;
        boolean filaEliminada;
        if (panelProductos.isVisible()) {
            filaSeleccionada = tblProductos.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblProductos.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblProductos.getColumnName(0);
                String nombreTabla = "productos";
                filaEliminada = DeleteFromTable.delete(nombreTabla, nombreColumna, id);  // Cambiado a "id"
                if (filaEliminada)mdlProductos.removeRow(filaSeleccionada);
            }
        } else if (panelProvedores.isVisible()) {
            filaSeleccionada = tblProveedores.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblProveedores.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblProveedores.getColumnName(0);
                String nombreTabla = "proveedores";
                filaEliminada = DeleteFromTable.delete(nombreTabla, nombreColumna, id);  // Cambiado a "id"
                if (filaEliminada)mdlProvedores.removeRow(filaSeleccionada);
            }
        } else if (panelTrabajadores.isVisible()) {
            filaSeleccionada = tblTrabajadores.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblTrabajadores.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblTrabajadores.getColumnName(0);
                String nombreTabla = "Trabajador";
                filaEliminada = DeleteFromTable.delete(nombreTabla, nombreColumna, id);  // Cambiado a "id"
                if (filaEliminada)mdlTrabajadores.removeRow(filaSeleccionada);
            }
        } else if (panelStock.isVisible()) {
            filaSeleccionada = tblStock.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) tblStock.getValueAt(filaSeleccionada, 0);
                String nombreColumna = tblStock.getColumnName(0);
                String nombreTabla = "Stock";
                filaEliminada = DeleteFromTable.delete(nombreTabla, nombreColumna, id);  // Cambiado a "id"
                if (filaEliminada) mdlStock.removeRow(filaSeleccionada);
            }
        }
    }


    private void AddTableSelectionListener() {
        tblTrabajadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTrabajadores.getSelectionModel().addListSelectionListener(e -> {
            Eliminar.setEnabled(tblTrabajadores.getSelectedRow() != -1);
            Editar.setEnabled(tblTrabajadores.getSelectedRow() != -1);
        });

        tblProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProveedores.getSelectionModel().addListSelectionListener(e -> {
            Eliminar.setEnabled(tblProveedores.getSelectedRow() != -1);
            Editar.setEnabled(tblProveedores.getSelectedRow() != -1);
        });

        tblStock.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStock.getSelectionModel().addListSelectionListener(e -> {
            Eliminar.setEnabled(tblStock.getSelectedRow() != -1);
            Editar.setEnabled(tblStock.getSelectedRow() != -1);
        });

        tblProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            Eliminar.setEnabled(tblProductos.getSelectedRow() != -1);
            Editar.setEnabled(tblProductos.getSelectedRow() != -1);
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
