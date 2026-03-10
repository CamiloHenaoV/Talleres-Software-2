package com.unicauca;

/**
 * Punto de entrada de la aplicación.
 * Demuestra la generación de reportes HTML y JSON usando la arquitectura Microkernel + Pipeline.
 */
public class Application {

    public static void main(String[] args) {
        System.out.println("  Sistema de Reportes - Arquitectura Microkernel");

        AppointmentService appointmentService = new AppointmentService();
        ReportPluginManager pluginManager     = new ReportPluginManager();
        ReportService reportService           = new ReportService(appointmentService, pluginManager);


        System.out.println(">>> Generando reporte en formato HTML...");
        String htmlReport = reportService.generateHtmlReport();
        System.out.println("\nReporte en formato HTML:\n");
        System.out.println(htmlReport);

        System.out.println("\n");


        System.out.println(">>> Generando reporte en formato JSON...");
        String jsonReport = reportService.generateJsonReport();
        System.out.println("\nReporte en formato JSON:\n");
        System.out.println(jsonReport);

        System.out.println("  Reportes generados exitosamente.");
    }
}
