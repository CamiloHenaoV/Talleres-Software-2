package com.unicauca;

import java.util.ArrayList;
import java.util.List;

/**
 * Pipeline de procesamiento que encadena múltiples etapas (PipelineStage).
 * Cada etapa recibe la lista de citas  y el resultado
 * acumulado de la etapa anterior.
 */
public class ReportPipeline {

    private final List<PipelineStage> stages = new ArrayList<>();

    /** Agrega una etapa al final del pipeline. */
    public ReportPipeline addStage(PipelineStage stage) {
        stages.add(stage);
        return this;
    }

    /**
     * Ejecuta todas las etapas en orden.
     * El resultado de cada etapa se pasa como entrada a la siguiente.
     *
     * @param appointments lista inicial de citas
     * @return resultado final del pipeline
     */
    public String execute(List<Appointment> appointments) {
        String result = "";
        for (PipelineStage stage : stages) {
            result = stage.process(appointments, result);
        }
        return result;
    }
}
