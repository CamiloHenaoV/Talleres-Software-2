package co.unicauca.biblioteca.app;

import javax.swing.SwingUtilities;
import co.unicauca.biblioteca.core.kernel.Kernel;
import co.unicauca.biblioteca.model.repository.InMemoryBookRepository;
import co.unicauca.biblioteca.model.service.BookService;
import co.unicauca.biblioteca.controller.BookController;
import co.unicauca.biblioteca.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // --- Microkernel bootstrap ---
        Kernel kernel = Kernel.defaultKernel();
        kernel.loadPlugins(); // via Java ServiceLoader

        // --- Core (domain) ---
        var repo = new InMemoryBookRepository();
        var service = new BookService(repo, kernel.getEventBus(), kernel.getOptionalPersistence());
        var controller = new BookController(service);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(controller, kernel);
            frame.setVisible(true);
        });
    }
}
