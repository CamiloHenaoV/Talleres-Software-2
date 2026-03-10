package com.unicauca.pipeline;

import com.unicauca.Appointment;
import com.unicauca.PipelineStage;

import java.util.List;

/**
 * Etapa 4: Convierte el nombre del doctor a CamelCase (iniciales en mayúsculas).
 * Ejemplo: "dr. juan perez" → "Dr. Juan Perez"
 */
public class TransformCamelCaseDoctorStage implements PipelineStage {

    @Override
    public String process(List<Appointment> appointments, String input) {
        for (Appointment a : appointments) {
            a.setDoctorName(toCamelCase(a.getDoctorName()));
        }
        System.out.println("[TransformCamelCaseDoctorStage] Nombres de doctores transformados a CamelCase.");
        return input;
    }

    private String toCamelCase(String name) {
        if (name == null || name.isBlank()) return name;
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                // Preservar el punto al final de abreviaturas como "Dr."
                if (word.endsWith(".")) {
                    sb.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase());
                } else {
                    sb.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }
}
