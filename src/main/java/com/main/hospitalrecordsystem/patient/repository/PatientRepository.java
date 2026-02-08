package com.main.hospitalrecordsystem.patient.repository;

import com.main.hospitalrecordsystem.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByPhone(String phone);

    Optional<Patient> findByEmail(String email);

    List<Patient> findByAgeGreaterThan(Integer age);

}
