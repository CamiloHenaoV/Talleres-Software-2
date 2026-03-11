package co.unicauca.biblioteca.core.kernel;

import java.util.Optional;
import co.unicauca.biblioteca.core.spi.PersistenceProvider;
import co.unicauca.biblioteca.core.spi.ReportProvider;

/**
 * Service registry provided by the kernel to plugins and app.
 * This is the "global resource access" mentioned in microkernel slides.
 */
public final class KernelServices {
    private PersistenceProvider persistence;
    private ReportProvider report;

    public void registerPersistence(PersistenceProvider provider) {
        this.persistence = provider;
    }

    public void registerReportProvider(ReportProvider provider) {
        this.report = provider;
    }

    public Optional<PersistenceProvider> getPersistence() {
        return Optional.ofNullable(persistence);
    }

    public Optional<ReportProvider> getReportProvider() {
        return Optional.ofNullable(report);
    }
}
