package com.main.hospitalrecordsystem.prescription.repository;

import com.main.hospitalrecordsystem.prescription.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
}
