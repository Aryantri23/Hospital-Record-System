package com.main.hospitalrecordsystem.medicalrecord.repository;

import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    List<MedicalRecord> findByPatient_Id(Integer patientId);

    List<MedicalRecord> findByDoctor_Id(Integer doctorId);

    Optional<MedicalRecord> findByPatient_IdAndVisitDate(Integer patientId, LocalDate date);

    List<MedicalRecord> findByVisitDate(LocalDate date);
}
