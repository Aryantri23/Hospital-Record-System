package com.main.hospitalrecordsystem.doctor.repository;

import com.main.hospitalrecordsystem.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByDepartment_Id(Integer department_Id);

}
