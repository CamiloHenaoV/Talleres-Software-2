package com.unicauca.pipeline;

import com.unicauca.Appointment;
import com.unicauca.PipelineStage;

import java.util.List;

/**
 * Etapa 2: Formatea la cédula añadiendo puntos de miles.
 * Ejemplo: 1002985158 → 1.002.985.158
 */
public class CedulaFormatter implements PipelineStage {

    @Override
    public String process(List<Appointment> appointments, String input) {
        for (Appointment a : appointments) {
            a.setPatientId(formatCedula(a.getPatientId()));
        }
        System.out.println("[CedulaFormatter] Cédulas formateadas con puntos de miles.");
        return input;
    }

    private String formatCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) return cedula;
        // Eliminar puntos existentes antes de reformatear
        String digits = cedula.replace(".", "").trim();
        StringBuilder sb = new StringBuilder(digits);
        int insertPosition = sb.length() - 3;
        while (insertPosition > 0) {
            sb.insert(insertPosition, '.');
            insertPosition -= 3;
        }
        return sb.toString();
    }
}
