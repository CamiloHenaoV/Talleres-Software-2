package co.unicauca.biblioteca.core.events;

public final class LibraryEvent {
    private final LibraryEventType type;
    private final String message;

    public LibraryEvent(LibraryEventType type, String message) {
        this.type = type;
        this.message = message;
    }

    public LibraryEventType getType() { return type; }
    public String getMessage() { return message; }
}
