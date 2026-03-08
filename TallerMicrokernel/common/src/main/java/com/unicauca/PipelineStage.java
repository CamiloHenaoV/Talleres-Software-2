package com.unicauca;

import java.util.List;

public interface PipelineStage {
    String process(List<Appointment> appointments, String input);
}
