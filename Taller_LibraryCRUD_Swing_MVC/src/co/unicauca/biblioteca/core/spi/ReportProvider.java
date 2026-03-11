package co.unicauca.biblioteca.core.spi;

import java.io.File;
import java.util.List;
import co.unicauca.biblioteca.model.entity.Book;

/**
 * Microkernel SPI for generating a report without changing the core.
 */
public interface ReportProvider {
    String name();
    File generateReport(List<Book> books) throws Exception;
}
