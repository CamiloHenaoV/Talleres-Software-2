package com.unicauca;

import java.util.List;

/**
 * Plugin que genera reportes en formato HTML.
 */
public class HtmlReportPlugin implements IReportPlugin {

    @Override
    public String generateReport(List<Appointment> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"es\">\n");
        sb.append("<head>\n");
        sb.append("  <meta charset=\"UTF-8\">\n");
        sb.append("  <title>Reporte de Citas</title>\n");
        sb.append("  <style>\n");
        sb.append("    body { font-family: Arial, sans-serif; margin: 20px; }\n");
        sb.append("    table { border-collapse: collapse; width: 100%; }\n");
        sb.append("    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }\n");
        sb.append("    th { background-color: #4CAF50; color: white; }\n");
        sb.append("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
        sb.append("  </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("  <h1>Reporte de Citas Agendadas</h1>\n");
        sb.append("  <table>\n");
        sb.append("    <thead>\n");
        sb.append("      <tr>\n");
        sb.append("        <th>ID</th>\n");
        sb.append("        <th>Fecha de agendamiento</th>\n");
        sb.append("        <th>Nombre del solicitante</th>\n");
        sb.append("        <th>Identificacion</th>\n");
        sb.append("        <th>Doctor encargado</th>\n");
        sb.append("      </tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");

        for (Appointment a : data) {
            sb.append("      <tr>\n");
            sb.append("        <td>").append(a.getId()).append("</td>\n");
            sb.append("        <td>").append(a.getSchedulingDate()).append("</td>\n");
            sb.append("        <td>").append(a.getPatientName()).append("</td>\n");
            sb.append("        <td>CC ").append(a.getPatientId()).append("</td>\n");
            sb.append("        <td>").append(a.getDoctorName()).append("</td>\n");
            sb.append("      </tr>\n");
        }

        sb.append("    </tbody>\n");
        sb.append("  </table>\n");
        sb.append("</body>\n");
        sb.append("</html>");
        return sb.toString();
    }
}
