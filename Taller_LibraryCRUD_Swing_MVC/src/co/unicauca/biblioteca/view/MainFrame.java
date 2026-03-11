package co.unicauca.biblioteca.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import co.unicauca.biblioteca.controller.BookController;
import co.unicauca.biblioteca.core.events.LibraryEvent;
import co.unicauca.biblioteca.core.events.LibraryEventType;
import co.unicauca.biblioteca.core.events.LibraryObserver;
import co.unicauca.biblioteca.core.kernel.Kernel;

public class MainFrame extends JFrame implements LibraryObserver {
    private final BookController controller;
    private final Kernel kernel;

    private final BookTableModel tableModel = new BookTableModel();
    private final JTable table = new JTable(tableModel);
    private final BookFormPanel form = new BookFormPanel();
    private final JLabel lblStatus = new JLabel("Listo.");

    private String selectedIsbn = null;

    public MainFrame(BookController controller, Kernel kernel) {
        super("Biblioteca - CRUD Libros (Swing + MVC + SOLID + Observer + Microkernel)");
        this.controller = controller;
        this.kernel = kernel;

        kernel.getEventBus().subscribe(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(980, 520);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(8,8));
        add(buildLeft(), BorderLayout.CENTER);
        add(buildRight(), BorderLayout.EAST);
        add(buildBottom(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JComponent buildLeft() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                var b = tableModel.getAt(table.getSelectedRow());
                selectedIsbn = b.getIsbn();
                form.setForm(b.getTitle(), b.getAuthor(), b.getIsbn(), b.getReservations());
            }
        });

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNew = new JButton("Nuevo");
        btnNew.addActionListener(e -> {
            selectedIsbn = null;
            form.clear();
            lblStatus.setText("Nuevo registro.");
        });
        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> onDelete());
        top.add(btnNew);
        top.add(btnDelete);

        p.add(top, BorderLayout.NORTH);
        return p;
    }

    private JComponent buildRight() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        p.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(0,1,6,6));
        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(e -> onSave());
        JButton btnExport = new JButton("Exportar reporte");
        btnExport.addActionListener(e -> onExportReport());

        actions.add(btnSave);
        actions.add(btnExport);

        p.add(actions, BorderLayout.SOUTH);
        return p;
    }

    private JComponent buildBottom() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(4,8,8,8));
        p.add(lblStatus, BorderLayout.CENTER);
        return p;
    }

    private void onSave() {
        try {
            String title = form.title();
            String author = form.author();
            String isbn = form.isbn();
            int reservations = form.reservations();

            if (title.isBlank() || author.isBlank() || isbn.isBlank()) {
                JOptionPane.showMessageDialog(this, "Título, autor e ISBN son obligatorios.");
                return;
            }

            if (selectedIsbn == null) {
                controller.create(title, author, isbn, reservations);
            } else {
                controller.update(selectedIsbn, title, author, isbn, reservations);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        if (selectedIsbn == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar ISBN " + selectedIsbn + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                controller.delete(selectedIsbn);
                selectedIsbn = null;
                form.clear();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExportReport() {
        var opt = kernel.getServices().getReportProvider();
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay plugin de reporte cargado.");
            return;
        }
        try {
            File f = opt.get().generateReport(controller.list());
            JOptionPane.showMessageDialog(this, "Reporte generado: " + f.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setData(controller.list());
    }

    @Override
    public void onEvent(LibraryEvent event) {
        if (event.getType() == LibraryEventType.BOOKS_CHANGED) {
            refreshTable();
        }
        lblStatus.setText(event.getMessage());
    }
}
