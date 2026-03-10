package com.unicauca.pipeline;

import com.unicauca.Appointment;
import com.unicauca.IReportPlugin;
import com.unicauca.PipelineStage;
import com.unicauca.ReportPluginManager;

import java.util.List;

/**
 * Etapa final del pipeline: invoca el plugin correspondiente para generar el reporte.
 * Usa el ReportPluginManager (reflexión) para cargar el plugin dinámicamente.
 */
public class PluginStage implements PipelineStage {

    private final String pluginKey;
    private final ReportPluginManager pluginManager;

    public PluginStage(String pluginKey, ReportPluginManager pluginManager) {
        this.pluginKey     = pluginKey;
        this.pluginManager = pluginManager;
    }

    @Override
    public String process(List<Appointment> appointments, String input) {
        IReportPlugin plugin = pluginManager.loadPlugin(pluginKey);
        if (plugin == null) {
            return "[PluginStage] Error: no se pudo cargar el plugin '" + pluginKey + "'.";
        }
        System.out.println("[PluginStage] Ejecutando plugin: " + plugin.getClass().getSimpleName());
        return plugin.generateReport(appointments);
    }
}
