package com.unicauca;

import com.unicauca.pipeline.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio principal de reportes.
 * Construye y ejecuta pipelines para generar reportes en diferentes formatos.
 */
public class ReportService {

    private final AppointmentService appointmentService;
    private final ReportPluginManager pluginManager;

    public ReportService(AppointmentService appointmentService,
                         ReportPluginManager pluginManager) {
        this.appointmentService = appointmentService;
        this.pluginManager      = pluginManager;
    }

    /**
     * Genera un reporte en el formato especificado mediante un pipeline de etapas.
     *
     * @param format clave del plugin ("html" o "json")
     * @return reporte generado como String
     */
    private String generateReport(String format) {
        // Copia independiente de los datos para no contaminar entre llamadas
        List<Appointment> data = deepCopy(appointmentService.getAll());

        ReportPipeline pipeline = new ReportPipeline()
            .addStage(new ValidationStage())
            .addStage(new CedulaFormatter())
            .addStage(new TransformCamelCaseNameStage())
            .addStage(new TransformCamelCaseDoctorStage())
            .addStage(new TransformDateStage())
            .addStage(new PluginStage(format, pluginManager));

        return pipeline.execute(data);
    }

    /** Genera reporte HTML. */
    public String generateHtmlReport() {
        return generateReport("html");
    }

    /** Genera reporte JSON. */
    public String generateJsonReport() {
        return generateReport("json");
    }

    /** Copia profunda de la lista para aislar transformaciones entre pipelines. */
    private List<Appointment> deepCopy(List<Appointment> source) {
        List<Appointment> copy = new ArrayList<>();
        for (Appointment a : source) {
            copy.add(new Appointment(
                a.getId(),
                a.getSchedulingDate(),
                a.getPatientName(),
                a.getPatientId(),
                a.getDoctorName()
            ));
        }
        return copy;
    }
}
