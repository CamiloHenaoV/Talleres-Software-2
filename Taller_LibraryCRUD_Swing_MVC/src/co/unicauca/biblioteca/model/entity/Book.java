package co.unicauca.biblioteca.model.entity;

import java.util.Objects;

/**
 * Entidad de negocio (según el diagrama: Titulo / Titulo del libro).
 * Para el CRUD trabajamos con un 'Libro' con datos básicos.
 */
public class Book {
    private String title;
    private String author;
    private String isbn;
    private int reservations;

    public Book(String title, String author, String isbn, int reservations) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.reservations = reservations;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getReservations() { return reservations; }
    public void setReservations(int reservations) { this.reservations = reservations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
