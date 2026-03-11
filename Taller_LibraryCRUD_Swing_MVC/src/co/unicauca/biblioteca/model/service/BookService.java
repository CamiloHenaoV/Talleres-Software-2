package co.unicauca.biblioteca.model.service;

import java.util.List;
import java.util.Optional;

import co.unicauca.biblioteca.core.events.LibraryEvent;
import co.unicauca.biblioteca.core.events.LibraryEventBus;
import co.unicauca.biblioteca.core.events.LibraryEventType;
import co.unicauca.biblioteca.core.spi.PersistenceProvider;
import co.unicauca.biblioteca.model.entity.Book;
import co.unicauca.biblioteca.model.repository.BookRepository;

/**
 * (SRP) Orquesta reglas de negocio del CRUD.
 * Publica eventos -> Observer (Views).
 * Opcionalmente usa un PersistenceProvider (plugin, microkernel).
 */
public class BookService {
    private final BookRepository repository;
    private final LibraryEventBus eventBus;
    private final Optional<PersistenceProvider> persistence;

    public BookService(BookRepository repository, LibraryEventBus eventBus, Optional<PersistenceProvider> persistence) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.persistence = persistence;

        // Cargar datos desde plugin si está disponible
        persistence.ifPresent(p -> {
            try {
                List<Book> loaded = p.load();
                if (loaded != null && !loaded.isEmpty()) {
                    loaded.forEach(repository::add);
                    eventBus.publish(new LibraryEvent(LibraryEventType.STATUS_MESSAGE,
                        "Datos cargados desde plugin: " + p.name()));
                }
            } catch (Exception ex) {
                eventBus.publish(new LibraryEvent(LibraryEventType.STATUS_MESSAGE,
                    "No se pudo cargar desde plugin: " + ex.getMessage()));
            }
        });
    }

    public List<Book> list() {
        return repository.findAll();
    }

    public void create(Book book) {
        repository.add(book);
        persistAndNotify("Libro creado.");
    }

    public void update(String originalIsbn, Book updated) {
        repository.update(originalIsbn, updated);
        persistAndNotify("Libro actualizado.");
    }

    public void delete(String isbn) {
        repository.delete(isbn);
        persistAndNotify("Libro eliminado.");
    }

    public Optional<Book> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

    private void persistAndNotify(String message) {
        persistence.ifPresent(p -> {
            try {
                p.save(repository.findAll());
            } catch (Exception ex) {
                eventBus.publish(new LibraryEvent(LibraryEventType.STATUS_MESSAGE,
                    "Error guardando (" + p.name() + "): " + ex.getMessage()));
            }
        });
        eventBus.publish(new LibraryEvent(LibraryEventType.BOOKS_CHANGED, message));
    }
}
