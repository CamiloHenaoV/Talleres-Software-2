package com.taller2solid.presentation;

import com.taller2solid.domain.User;
import com.taller2solid.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private final UserService userService;
    private final User loggedUser;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnCreate;
    private JButton btnEdit;
    private JButton btnDelete;

    public MainFrame(UserService userService, User loggedUser) {
        this.userService = userService;
        this.loggedUser = loggedUser;

        initComponents();
        loadUsers();
        applyRolePermissions();
    }

    private void initComponents() {

        setTitle("Sistema de Gestión de Usuarios");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 🔹 Información del usuario logueado
        JLabel lblUserInfo = new JLabel(
                "Usuario: " + loggedUser.getUsername() +
                        " | Rol: " + loggedUser.getRole()
        );
        lblUserInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblUserInfo, BorderLayout.NORTH);

        // 🔹 Tabla
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Username", "Email", "Rol"}, 0
        );
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Panel de botones
        JPanel buttonPanel = new JPanel();

        btnCreate = new JButton("Crear");
        btnEdit = new JButton("Editar");
        btnDelete = new JButton("Eliminar");
        JButton btnRefresh = new JButton("Refrescar");
        JButton btnLogout = new JButton("Cerrar Sesión");

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnLogout);

        add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Eventos
        btnCreate.addActionListener(e -> createUser());
        btnEdit.addActionListener(e -> editUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnRefresh.addActionListener(e -> loadUsers());
        btnLogout.addActionListener(e -> logout());
    }

    private void loadUsers() {
        tableModel.setRowCount(0);

        List<User> users = userService.findAllUsers();

        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getRole()
            });
        }
    }

    private void createUser() {
        new UserFormDialog(this, userService, null).setVisible(true);
        loadUsers();
    }

    private void editUser() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario");
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);

        userService.findUserById(id).ifPresent(user -> {
            new UserFormDialog(this, userService, user).setVisible(true);
            loadUsers();
        });
    }

    private void deleteUser() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario");
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este usuario?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            UserService.ServiceResult result = userService.deleteUser(id);
            JOptionPane.showMessageDialog(this, result.getMessage());
            loadUsers();
        }
    }

    private void applyRolePermissions() {

        if (loggedUser.getRole().name().equals("USER")) {
            btnCreate.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        // ADMIN tiene todo habilitado
    }

    private void logout() {
        dispose();
        new LoginFrame(userService).setVisible(true);
    }
}
