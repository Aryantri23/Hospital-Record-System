package com.main.hospitalrecordsystem.doctor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.department.entity.Department;
import com.main.hospitalrecordsystem.enums.Days;
import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(
            name = "department_id",
            nullable = false
    )
    private Department department;

    private String specialization;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Days> availableDays;

    @JsonIgnore
    @OneToMany(
            mappedBy = "doctor"
    )
    private List<MedicalRecord> medicalRecords;

    @JsonIgnore
    @OneToMany(
            mappedBy = "doctor"
    )
    private List<Appointment> appointments;

    public Department getDepartment() {
        return department;
    }



    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<Days> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Set<Days> availableDays) {
        this.availableDays = availableDays;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
