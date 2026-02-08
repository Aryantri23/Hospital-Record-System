package com.main.hospitalrecordsystem.patient.controller;

import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.patient.entity.Patient;
import com.main.hospitalrecordsystem.patient.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<Patient>> savePatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Patient>>> findAllPatient() {
        return patientService.findAllPatient();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseStructure<Patient>> findPatientById(@PathVariable Integer id) {
        return patientService.findPatientById(id);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ResponseStructure<Patient>> findPatientByPhone(@PathVariable String phone) {
        return patientService.findPatientByPhone(phone);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<ResponseStructure<List<Patient>>> findByAgeGreater(@PathVariable Integer age) {
        return patientService.findByAgeGreater(age);
    }

    @PutMapping
    public ResponseEntity<ResponseStructure<Patient>> updatePatient(@RequestBody Patient patient) {
        return patientService.updatePatient(patient);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<ResponseStructure<String>> deletePatient(@PathVariable Integer id) {
        return patientService.deletePatient(id);
    }
}

