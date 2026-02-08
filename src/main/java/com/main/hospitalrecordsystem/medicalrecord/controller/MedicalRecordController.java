package com.main.hospitalrecordsystem.medicalrecord.controller;

import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import com.main.hospitalrecordsystem.medicalrecord.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
