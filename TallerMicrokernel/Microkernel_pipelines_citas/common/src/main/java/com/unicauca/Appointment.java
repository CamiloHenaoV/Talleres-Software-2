package com.unicauca;

public class Appointment {
    private String id;
    private String schedulingDate; // formato yyyy-MM-dd
    private String patientName;
    private String patientId;      // cédula numérica sin puntos
    private String doctorName;

    public Appointment(String id, String schedulingDate, String patientName,
                       String patientId, String doctorName) {
        this.id = id;
        this.schedulingDate = schedulingDate;
        this.patientName = patientName;
        this.patientId = patientId;
        this.doctorName = doctorName;
    }

    public String getId()             { return id; }
    public String getSchedulingDate() { return schedulingDate; }
    public String getPatientName()    { return patientName; }
    public String getPatientId()      { return patientId; }
    public String getDoctorName()     { return doctorName; }

    public void setSchedulingDate(String schedulingDate) { this.schedulingDate = schedulingDate; }
    public void setPatientName(String patientName)       { this.patientName = patientName; }
    public void setPatientId(String patientId)           { this.patientId = patientId; }
    public void setDoctorName(String doctorName)         { this.doctorName = doctorName; }
}
