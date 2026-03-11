package co.unicauca.biblioteca.core.kernel;

/**
 * Microkernel plugin contract.
 * Plugins are loaded by ServiceLoader without changing the core.
 */
public interface Plugin {
    String id();
    void start(PluginContext context);
}
