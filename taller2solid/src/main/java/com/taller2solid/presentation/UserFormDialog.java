package com.taller2solid.presentation;

import com.taller2solid.domain.Role;
import com.taller2solid.domain.User;
import com.taller2solid.service.UserService;

import javax.swing.*;
import java.awt.*;

public class UserFormDialog extends JDialog {

    private final UserService userService;
    private final User user;

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<Role> cmbRole;

    public UserFormDialog(JFrame parent,
                          UserService userService,
                          User user) {
        super(parent, true);
        this.userService = userService;
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        setTitle(user == null ? "Crear Usuario" : "Editar Usuario");
        setSize(350, 250);
        setLocationRelativeTo(getParent());
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Username:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        add(new JLabel("Rol:"));
        cmbRole = new JComboBox<>(Role.values());
        add(cmbRole);

        JButton btnSave = new JButton("Guardar");
        add(new JLabel());
        add(btnSave);

        if (user != null) {
            txtUsername.setText(user.getUsername());
            txtEmail.setText(user.getEmail());
            cmbRole.setSelectedItem(user.getRole());
        }

        btnSave.addActionListener(e -> saveUser());
    }

    private void saveUser() {
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        Role role = (Role) cmbRole.getSelectedItem();

        User newUser = new User(
                user == null ? null : user.getId(),
                username,
                password,
                email,
                role
        );

        UserService.ServiceResult result;

        if (user == null) {
            result = userService.createUser(newUser);
        } else {
            result = userService.updateUser(newUser);
        }

        JOptionPane.showMessageDialog(this, result.getMessage());

        if (result.isSuccess()) {
            dispose();
        }
    }
}
