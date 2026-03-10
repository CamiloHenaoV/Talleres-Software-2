package com.unicauca.pipeline;

import com.unicauca.Appointment;
import com.unicauca.PipelineStage;

import java.util.List;

/**
 * Etapa 1: Valida que la lista de citas no esté vacía.
 * Lanza excepción en tiempo de ejecución si la validación falla.
 */
public class ValidationStage implements PipelineStage {

    @Override
    public String process(List<Appointment> appointments, String input) {
        if (appointments == null || appointments.isEmpty()) {
            throw new IllegalArgumentException(
                "[ValidationStage] Error: la lista de citas está vacía. No se puede generar el reporte.");
        }
        System.out.println("[ValidationStage] OK - " + appointments.size() + " cita(s) encontrada(s).");
        return input; // sin transformación de texto; solo valida
    }
}
