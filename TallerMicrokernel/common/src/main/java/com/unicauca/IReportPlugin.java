package com.unicauca;

import java.util.List;

public interface IReportPlugin {
    String generateReport(List<Appointment> data);
}
