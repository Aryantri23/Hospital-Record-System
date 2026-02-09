package com.main.hospitalrecordsystem.medicalrecord.controller;

import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import com.main.hospitalrecordsystem.medicalrecord.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hospital/record")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<MedicalRecord>> saveRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.saveRecord(medicalRecord);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findAllMedicalRecord() {
        return medicalRecordService.findAllMedicalRecord();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseStructure<MedicalRecord>> findById(@PathVariable Integer id) {
        return medicalRecordService.findById(id);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByPatientId(@PathVariable Integer id) {
        return medicalRecordService.findByPatientId(id);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByDoctorId(@PathVariable Integer id) {
        return medicalRecordService.findByDoctorId(id);
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<ResponseStructure<MedicalRecord>> findByAppointmentId(@PathVariable Integer id) {
        return medicalRecordService.findByAppointmentId(id);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByDate(@PathVariable LocalDate date) {
        return medicalRecordService.findByDate(date);
    }
}
