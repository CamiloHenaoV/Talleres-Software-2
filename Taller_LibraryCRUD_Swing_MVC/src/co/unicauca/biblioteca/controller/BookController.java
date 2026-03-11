package co.unicauca.biblioteca.controller;

import java.util.List;
import java.util.Optional;

import co.unicauca.biblioteca.model.entity.Book;
import co.unicauca.biblioteca.model.service.BookService;

/**
 * Controller (MVC): adapta acciones de la UI a la capa de servicio.
 * (SRP) Solo coordina entrada/salida, no reglas de negocio.
 */
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    public List<Book> list() {
        return service.list();
    }

    public void create(String title, String author, String isbn, int reservations) {
        service.create(new Book(title, author, isbn, reservations));
    }

    public void update(String originalIsbn, String title, String author, String isbn, int reservations) {
        service.update(originalIsbn, new Book(title, author, isbn, reservations));
    }

    public void delete(String isbn) {
        service.delete(isbn);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return service.findByIsbn(isbn);
    }
    public int getTotalBooks()    { return service.getTotalBooks(); }
    public String getLastAction() { return service.getLastAction(); }
    public String getLastTime()   { return service.getLastTime(); }
}
