package DemoInv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainApp {

    public static void main(String[] args) {
        /*
        JFrame frame = new JFrame("Datos de MySQL en JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Crear el JTable
        JTable table = new JTable(new DefaultTableModel());
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Cargar los datos desde la base de datos
        DatabaseToTable.cargarDatos(model, "select * from productos");

        frame.setVisible(true);
        */
         String valor = "1.5";

         Double valorint = Double.parseDouble(valor);

        System.out.println(valorint);
    }
}
