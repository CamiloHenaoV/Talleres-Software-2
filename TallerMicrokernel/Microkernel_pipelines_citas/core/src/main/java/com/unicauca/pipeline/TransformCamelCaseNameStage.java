package com.unicauca.pipeline;

import com.unicauca.Appointment;
import com.unicauca.PipelineStage;

import java.util.List;

/**
 * Etapa 3: Convierte el nombre del paciente a CamelCase (iniciales en mayúsculas).
 * Ejemplo: "maria lopez" → "Maria Lopez"
 */
public class TransformCamelCaseNameStage implements PipelineStage {

    @Override
    public String process(List<Appointment> appointments, String input) {
        for (Appointment a : appointments) {
            a.setPatientName(toCamelCase(a.getPatientName()));
        }
        System.out.println("[TransformCamelCaseNameStage] Nombres de pacientes transformados a CamelCase.");
        return input;
    }

    private String toCamelCase(String name) {
        if (name == null || name.isBlank()) return name;
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }
}
