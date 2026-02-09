package com.main.hospitalrecordsystem.prescription.controller;

import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.prescription.entity.Prescription;
import com.main.hospitalrecordsystem.prescription.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital/prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/record/{id}")
    public ResponseEntity<ResponseStructure<Prescription>> savePrescription(@PathVariable Integer id, @RequestBody Prescription prescription) {
        return prescriptionService.savePrescription(id, prescription);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Prescription>>> findAllPrescription() {
        return prescriptionService.findAllPrescription();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseStructure<Prescription>> findById(@PathVariable Integer id) {
        return prescriptionService.findById(id);
    }

    @GetMapping("/record/{id}")
    public ResponseEntity<ResponseStructure<Prescription>> findByRecordId(@PathVariable Integer id) {
        return prescriptionService.findByRecordId(id);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<ResponseStructure<List<Prescription>>> findByPatientId(@PathVariable Integer id) {
        return prescriptionService.findByPatientId(id);
    }
}
