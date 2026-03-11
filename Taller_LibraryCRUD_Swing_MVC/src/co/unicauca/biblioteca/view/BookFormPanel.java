package co.unicauca.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class BookFormPanel extends JPanel {
    private final JTextField txtTitle = new JTextField(20);
    private final JTextField txtAuthor = new JTextField(20);
    private final JTextField txtIsbn = new JTextField(20);
    private final JSpinner spReservations = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));

    public BookFormPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        int r=0;
        add(new JLabel("Título:"), gbc(c,0,r));
        add(txtTitle, gbc(c,1,r++));
        add(new JLabel("Autor:"), gbc(c,0,r));
        add(txtAuthor, gbc(c,1,r++));
        add(new JLabel("ISBN:"), gbc(c,0,r));
        add(txtIsbn, gbc(c,1,r++));
        add(new JLabel("#Reservas:"), gbc(c,0,r));
        add(spReservations, gbc(c,1,r++));
    }

    private GridBagConstraints gbc(GridBagConstraints base, int x, int y) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x; c.gridy = y;
        return c;
    }

    public String title() { return txtTitle.getText().trim(); }
    public String author() { return txtAuthor.getText().trim(); }
    public String isbn() { return txtIsbn.getText().trim(); }
    public int reservations() { return (Integer) spReservations.getValue(); }

    public void setForm(String title, String author, String isbn, int reservations) {
        txtTitle.setText(title);
        txtAuthor.setText(author);
        txtIsbn.setText(isbn);
        spReservations.setValue(reservations);
    }

    public void clear() {
        setForm("", "", "", 0);
    }
}
