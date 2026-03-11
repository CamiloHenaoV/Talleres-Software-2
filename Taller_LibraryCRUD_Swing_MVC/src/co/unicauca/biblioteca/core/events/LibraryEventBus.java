package co.unicauca.biblioteca.core.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Observer pattern: Subject / publisher of events. Views subscribe, model/service publishes.
 */
public final class LibraryEventBus {
    private final List<LibraryObserver> observers = new CopyOnWriteArrayList<>();

    public void subscribe(LibraryObserver observer) {
        if (observer != null) observers.add(observer);
    }

    public void unsubscribe(LibraryObserver observer) {
        observers.remove(observer);
    }

    public void publish(LibraryEvent event) {
        for (var o : observers) {
            o.onEvent(event);
        }
    }
}
