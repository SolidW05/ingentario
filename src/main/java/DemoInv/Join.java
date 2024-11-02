package DemoInv;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Join extends JFrame {
    private JTextField usuario;
    private JPasswordField contraseña;
    private JButton entrarButton;
    private JLabel Titulo;
    private JPanel ventanaJoin;
    private JLabel img;

    private personaVO persona;

    Join(){

        add(ventanaJoin);
        pack();
        entrarButton.addActionListener(e -> validarCredenciales());
        usuario.setToolTipText("Ingrese su usuario");
        contraseña.setToolTipText("Ingrese su contraseña");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        agregarImagenes();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void validarCredenciales() {
        if (join()) {
            JOptionPane.showMessageDialog(this, "Acceso permitido. Bienvenido!");
            dispose();
            new VentanaPrincipal();
            // Lógica para abrir la ventana correspondiente según el rol
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }

    private boolean join(){

        String usuario = this.usuario.getText();
        String contraseña = new String(this.contraseña.getPassword());
        String query = "SELECT nombre, administrador FROM Trabajador WHERE nombre = ? AND passwd = ?";

        try (Connection conn = DatabaseToTable.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                persona = new personaVO();
                persona.setNombre(rs.getString("nombre"));
                persona.setAdministrador(rs.getInt("administrador")  == 1);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;

    }

    private void agregarImagenes(){
        ImageIcon imagen = new ImageIcon(new ImageIcon("src/ImagenesButton/Inventario.png")
                .getImage().getScaledInstance(200,200, Image.SCALE_SMOOTH));
        img.setIcon(imagen);
    }

    public static void main(String[] args) {
        new Join();
    }

}
