package co.unicauca.biblioteca.core.spi;

import java.util.List;
import co.unicauca.biblioteca.model.entity.Book;

/**
 * Microkernel SPI (Service Provider Interface) for persistence.
 * A plugin can provide a concrete implementation (CSV, DB, etc).
 */
public interface PersistenceProvider {
    String name();
    List<Book> load();
    void save(List<Book> books);
}
