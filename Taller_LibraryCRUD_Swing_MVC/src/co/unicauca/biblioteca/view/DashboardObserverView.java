package co.unicauca.biblioteca.view;

import co.unicauca.biblioteca.controller.BookController;
import co.unicauca.biblioteca.core.events.LibraryEvent;
import co.unicauca.biblioteca.core.events.LibraryEventBus;
import co.unicauca.biblioteca.core.events.LibraryEventType;
import co.unicauca.biblioteca.core.events.LibraryObserver;

import javax.swing.*;
import java.awt.*;

/**
 * Vista Dashboard acoplada al MainFrame que implementa LibraryObserver.
 * Su única responsabilidad es pintar los datos que le entrega el Controller.
 */
public class DashboardObserverView extends JPanel implements LibraryObserver {

    private final BookController controller;

    private final JLabel lblTotal      = new JLabel("0");
    private final JLabel lblLastAction = new JLabel("—");
    private final JLabel lblLastTime   = new JLabel("—");

    public DashboardObserverView(BookController controller, LibraryEventBus eventBus) {
        this.controller = controller;

        eventBus.subscribe(this);

        setBorder(BorderFactory.createTitledBorder("Dashboard - Monitor de Eventos"));
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));

        add(new JLabel("Total de libros:")); add(lblTotal);
        add(new JLabel("  |  Última acción:")); add(lblLastAction);
        add(new JLabel("  |  Hora:")); add(lblLastTime);

        refresh();
    }

    @Override
    public void onEvent(LibraryEvent event) {
        LibraryEventType type = event.getType();
        if (type == LibraryEventType.BOOKS_CHANGED
                || type == LibraryEventType.BOOK_CREATED
                || type == LibraryEventType.BOOK_UPDATED
                || type == LibraryEventType.BOOK_DELETED) {
            SwingUtilities.invokeLater(this::refresh);
        }
    }

    private void refresh() {
        lblTotal.setText(String.valueOf(controller.getTotalBooks()));
        lblLastAction.setText(controller.getLastAction());
        lblLastTime.setText(controller.getLastTime());
    }
}