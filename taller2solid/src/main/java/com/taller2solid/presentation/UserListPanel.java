package com.taller2solid.presentation;

import com.taller2solid.domain.User;
import com.taller2solid.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserListPanel extends JPanel {

    private final UserService userService;

    private JTable table;
    private DefaultTableModel tableModel;

    public UserListPanel(UserService userService) {
        this.userService = userService;
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Username", "Email", "Rol"}, 0);

        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadUsers() {
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
}
