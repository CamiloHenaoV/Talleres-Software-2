package co.unicauca.biblioteca.core.kernel;

import co.unicauca.biblioteca.core.events.LibraryEventBus;

public final class PluginContext {
    private final LibraryEventBus eventBus;
    private final KernelServices services;

    PluginContext(LibraryEventBus eventBus, KernelServices services) {
        this.eventBus = eventBus;
        this.services = services;
    }

    public LibraryEventBus eventBus() { return eventBus; }
    public KernelServices services() { return services; }
}
