package com.unicauca;

import java.io.InputStream;
import java.util.Properties;

/**
 * Fábrica que utiliza reflexión para crear dinámicamente los plugins registrados
 * en el archivo plugin.properties.
 */
public class ReportPluginManager {

    private final Properties properties = new Properties();

    public ReportPluginManager() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("plugin.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("[ReportPluginManager] plugin.properties no encontrado.");
            }
        } catch (Exception e) {
            System.err.println("[ReportPluginManager] Error cargando plugin.properties: " + e.getMessage());
        }
    }

    /**
     * Carga dinámicamente un plugin a partir de su clave en plugin.properties.
     *
     * @param pluginKey clave del plugin (ej. "html", "json")
     * @return instancia del plugin o null si falla
     */
    public IReportPlugin loadPlugin(String pluginKey) {
        String className = properties.getProperty(pluginKey);
        if (className == null) {
            System.err.println("[ReportPluginManager] Plugin no registrado: " + pluginKey);
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            return (IReportPlugin) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("[ReportPluginManager] Error instanciando plugin '" + pluginKey
                    + "' (" + className + "): " + e.getMessage());
            return null;
        }
    }
}
