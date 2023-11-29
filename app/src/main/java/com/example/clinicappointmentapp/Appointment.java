package com.example.clinicappointmentapp;
public class Appointment {
    private String appointmentDuration;
    private String appointmentTime;
    private String dateOfAppointment;
    private String doctorAssigned;

    // Empty constructor for Firebase
    public Appointment() {
    }

    public Appointment(String appointmentDuration, String appointmentTime, String dateOfAppointment, String doctorAssigned) {
        this.appointmentDuration = appointmentDuration;
        this.appointmentTime = appointmentTime;
        this.dateOfAppointment = dateOfAppointment;
        this.doctorAssigned = doctorAssigned;
    }

    public String getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(String appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(String dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getDoctorAssigned() {
        return doctorAssigned;
    }

    public void setDoctorAssigned(String doctorAssigned) {
        this.doctorAssigned = doctorAssigned;
    }
}
