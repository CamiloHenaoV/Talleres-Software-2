package co.unicauca.biblioteca.view;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import co.unicauca.biblioteca.model.entity.Book;

/**
 * View-model para JTable (Swing).
 */
public class BookTableModel extends AbstractTableModel {
    private final String[] columns = {"Título", "Autor", "ISBN", "#Reservas"};
    private List<Book> data = new ArrayList<>();

    public void setData(List<Book> data) {
        this.data = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public Book getAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book b = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> b.getTitle();
            case 1 -> b.getAuthor();
            case 2 -> b.getIsbn();
            case 3 -> b.getReservations();
            default -> "";
        };
    }
}
