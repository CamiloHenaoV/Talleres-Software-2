package co.unicauca.biblioteca.plugins.report.simple;

import co.unicauca.biblioteca.core.kernel.Plugin;
import co.unicauca.biblioteca.core.kernel.PluginContext;
import co.unicauca.biblioteca.core.spi.ReportProvider;
import co.unicauca.biblioteca.model.entity.Book;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Plugin (Microkernel) - Reporte TXT.
 */
public class SimpleTxtReportPlugin implements Plugin, ReportProvider {

    @Override
    public String id() {
        return "txt-report";
    }

    @Override
    public void start(PluginContext context) {
        context.services().registerReportProvider(this);
        context.eventBus().publish(new co.unicauca.biblioteca.core.events.LibraryEvent(
            co.unicauca.biblioteca.core.events.LibraryEventType.STATUS_MESSAGE,
            "Plugin activado: " + name()
        ));
    }

    @Override
    public String name() {
        return "TXT Report";
    }

    @Override
    public File generateReport(List<Book> books) throws Exception {
        File outDir = new File("reports");
        if (!outDir.exists()) outDir.mkdirs();
        File f = new File(outDir, "reporte_libros_" + System.currentTimeMillis() + ".txt");
        try (FileWriter w = new FileWriter(f)) {
            w.write("Reporte de Libros - " + LocalDateTime.now() + "\n");
            w.write("Total: " + books.size() + "\n\n");
            for (Book b : books) {
                w.write("- " + b.getTitle() + " | " + b.getAuthor() + " | " + b.getIsbn() + " | reservas=" + b.getReservations() + "\n");
            }
        }
        return f;
    }
}
