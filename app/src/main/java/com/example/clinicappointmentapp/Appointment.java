package com.example.clinicappointmentapp;

public class Appointment {
    private String name;
    private String age;
    private String sex;
    private String phone;
    private String residence;
    private String reason;
    private String submissionTime; // New field

    // Default constructor required for calls to DataSnapshot.getValue(Appointment.class)
    public Appointment() {}

    public Appointment(String name, String age, String sex, String phone, String residence, String reason, String submissionTime) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
        this.residence = residence;
        this.reason = reason;
        this.submissionTime = submissionTime;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getResidence() {
        return residence;
    }

    public String getReason() {
        return reason;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }
}
