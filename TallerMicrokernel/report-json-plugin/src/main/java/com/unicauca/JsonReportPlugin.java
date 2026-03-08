package com.unicauca;

import java.util.List;

/**
 * Plugin que genera reportes en formato JSON.
 */
public class JsonReportPlugin implements IReportPlugin {

    @Override
    public String generateReport(List<Appointment> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < data.size(); i++) {
            Appointment a = data.get(i);
            sb.append("  {\n");
            sb.append("    \"ID\": \"").append(escape(a.getId())).append("\",\n");
            sb.append("    \"Fecha de agendamiento\": \"").append(escape(a.getSchedulingDate())).append("\",\n");
            sb.append("    \"Nombre del solicitante\": \"").append(escape(a.getPatientName())).append("\",\n");
            sb.append("    \"Identificacion\": \"CC ").append(escape(a.getPatientId())).append("\",\n");
            sb.append("    \"Doctor encargado\": \"").append(escape(a.getDoctorName())).append("\"\n");
            sb.append("  }");
            if (i < data.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("]");
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
