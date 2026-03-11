package co.unicauca.biblioteca.model.repository;

import java.util.List;
import java.util.Optional;
import co.unicauca.biblioteca.model.entity.Book;

/**
 * Abstracción (DIP): la capa de servicio depende de esta interfaz, no de una clase concreta.
 */
public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findByIsbn(String isbn);
    void add(Book book);
    void update(String isbn, Book updated);
    void delete(String isbn);
}
