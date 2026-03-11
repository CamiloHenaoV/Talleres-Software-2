package co.unicauca.biblioteca.core.kernel;

import java.util.ServiceLoader;

import co.unicauca.biblioteca.core.events.LibraryEventBus;
import co.unicauca.biblioteca.core.events.LibraryEvent;
import co.unicauca.biblioteca.core.events.LibraryEventType;
import co.unicauca.biblioteca.core.spi.PersistenceProvider;

public final class Kernel {
    private final LibraryEventBus eventBus = new LibraryEventBus();
    private final KernelServices services = new KernelServices();

    private Kernel() {}

    public static Kernel defaultKernel() {
        return new Kernel();
    }

    public LibraryEventBus getEventBus() {
        return eventBus;
    }

    public KernelServices getServices() {
        return services;
    }

    public java.util.Optional<PersistenceProvider> getOptionalPersistence() {
        return services.getPersistence();
    }

    public void loadPlugins() {
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        PluginContext ctx = new PluginContext(eventBus, services);

        int count = 0;
        for (Plugin p : loader) {
            try {
                p.start(ctx);
                count++;
            } catch (Exception ex) {
                eventBus.publish(new LibraryEvent(
                    LibraryEventType.STATUS_MESSAGE,
                    "Plugin error (" + p.id() + "): " + ex.getMessage()
                ));
            }
        }
        eventBus.publish(new LibraryEvent(
            LibraryEventType.STATUS_MESSAGE,
            "Kernel: " + count + " plugin(s) loaded."
        ));
    }
}
