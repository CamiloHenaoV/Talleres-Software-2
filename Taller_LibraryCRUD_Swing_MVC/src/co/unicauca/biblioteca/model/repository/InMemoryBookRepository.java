package co.unicauca.biblioteca.model.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import co.unicauca.biblioteca.model.entity.Book;

/**
 * Implementación simple en memoria.
 * (SRP) Solo gestiona almacenamiento de libros.
 */
public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> data = new ConcurrentHashMap<>();

    public InMemoryBookRepository() {
        // Datos semilla
        add(new Book("Clean Code", "Robert C. Martin", "9780132350884", 0));
        add(new Book("Design Patterns", "GoF", "9780201633610", 1));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(data.get(isbn));
    }

    @Override
    public void add(Book book) {
        Objects.requireNonNull(book);
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN requerido.");
        }
        if (data.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Ya existe un libro con ese ISBN.");
        }
        data.put(book.getIsbn(), book);
    }

    @Override
    public void update(String isbn, Book updated) {
        Objects.requireNonNull(updated);
        if (!data.containsKey(isbn)) {
            throw new NoSuchElementException("No existe libro con ISBN: " + isbn);
        }
        // Si cambia el ISBN, validar colisiones:
        String newIsbn = updated.getIsbn();
        if (!Objects.equals(isbn, newIsbn) && data.containsKey(newIsbn)) {
            throw new IllegalArgumentException("El nuevo ISBN ya existe.");
        }
        data.remove(isbn);
        data.put(newIsbn, updated);
    }

    @Override
    public void delete(String isbn) {
        data.remove(isbn);
    }
}
