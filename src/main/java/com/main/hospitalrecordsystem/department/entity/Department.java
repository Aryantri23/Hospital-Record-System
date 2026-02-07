package com.main.hospitalrecordsystem.department.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.hospitalrecordsystem.doctor.entity.Doctor;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.REMOVE,
            mappedBy = "department"
    )
    private List<Doctor> doctors;

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
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
}
