package com.main.hospitalrecordsystem.patient.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.enums.Gender;
import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(
            unique = true,
            length = 10,
            nullable = false
    )
    private String phone;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    private List<MedicalRecord> medicalRecords;

    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
