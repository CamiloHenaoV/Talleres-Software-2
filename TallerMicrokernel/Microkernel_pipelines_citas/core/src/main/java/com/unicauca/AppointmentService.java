package com.unicauca;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que provee datos de citas agendadas.
 * Por simplicidad, devuelve una lista estática sin repositorio.
 */
public class AppointmentService {

    /**
     * Retorna una lista de citas de ejemplo.
     * Las cédulas se almacenan sin puntos; el pipeline se encargará de formatearlas.
     */
    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment("001", "2026-03-05", "maria lopez",   "1023456789", "dr. juan perez"));
        appointments.add(new Appointment("002", "2026-03-06", "carlos ramirez", "9876543210", "dra. ana torres"));
        appointments.add(new Appointment("003", "2026-03-07", "laura gomez",    "1122334455", "dr. andres castillo"));
        appointments.add(new Appointment("004", "2026-03-08", "pedro martinez", "5566778899", "dra. sofia herrera"));
        appointments.add(new Appointment("005", "2026-03-09", "juliana rios",   "2233445566", "dr. felipe morales"));
        return appointments;
    }
}
