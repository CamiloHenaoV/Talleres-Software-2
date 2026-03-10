package com.unicauca.pipeline;

import com.unicauca.Appointment;
import com.unicauca.PipelineStage;

import java.util.List;
import java.util.Locale;

/**
 * Etapa 5: Transforma la fecha de agendamiento al formato dd-mmm-aaaa.
 * Ejemplo: "2026-03-23" → "23-mar-2026"
 */
public class TransformDateStage implements PipelineStage {

    private static final String[] MONTHS = {
        "ene", "feb", "mar", "abr", "may", "jun",
        "jul", "ago", "sep", "oct", "nov", "dic"
    };

    @Override
    public String process(List<Appointment> appointments, String input) {
        for (Appointment a : appointments) {
            a.setSchedulingDate(transformDate(a.getSchedulingDate()));
        }
        System.out.println("[TransformDateStage] Fechas transformadas a formato dd-mmm-aaaa.");
        return input;
    }

    /**
     * Convierte "yyyy-MM-dd" → "dd-mmm-yyyy".
     */
    private String transformDate(String date) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) return date;
        String[] parts = date.split("-");
        int year  = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day   = Integer.parseInt(parts[2]);
        String monthName = (month >= 1 && month <= 12) ? MONTHS[month - 1] : "???";
        return String.format("%02d-%s-%04d", day, monthName, year);
    }
}
