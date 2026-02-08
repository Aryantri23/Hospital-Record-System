package com.main.hospitalrecordsystem.medicalrecord.repository;

import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

}
