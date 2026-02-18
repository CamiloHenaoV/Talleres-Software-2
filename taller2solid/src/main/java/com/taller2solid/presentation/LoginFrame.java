package com.taller2solid.presentation;

import com.taller2solid.domain.User;
import com.taller2solid.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserService userService;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame(UserService userService) {
        this.userService = userService;
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Sistema de Usuarios");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Ingresar");
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login());
    }

    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar usuario y contraseña");
            return;
        }

        UserService.ServiceResult result =
                userService.authenticate(username, password);

        if (result.isSuccess()) {

            User loggedUser = result.getUser();

            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + loggedUser.getUsername() +
                            "\nRol: " + loggedUser.getRole());

            dispose();

            // 🔥 AQUÍ PASAMOS EL USUARIO AL MAINFRAME
            new MainFrame(userService, loggedUser).setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, result.getMessage());
        }
    }
}
