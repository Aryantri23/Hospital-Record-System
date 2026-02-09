package com.main.hospitalrecordsystem.doctor.controller;

import com.main.hospitalrecordsystem.doctor.entity.Doctor;
import com.main.hospitalrecordsystem.doctor.service.DoctorService;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.enums.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/hospital/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<Doctor>> saveDoctor(@RequestBody Doctor doctor) {
        return doctorService.saveDoctor(doctor);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Doctor>>> findAllDoctor() {
        return doctorService.findAllDoctor();
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<ResponseStructure<List<Doctor>>> findDoctorBySpecialization(@PathVariable String specialization) {
        return doctorService.findDoctorBySpecialization(specialization);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseStructure<List<Doctor>>> findByDepartment(@PathVariable String department) {
        return doctorService.findByDepartment(department);
    }

    @GetMapping("/days")
    public ResponseEntity<ResponseStructure<Set<Doctor>>> findDoctorByAvailableDays(@RequestBody Set<Days> days) {
        return doctorService.findDoctorByAvailableDays(days);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Doctor>> findDoctorById(@PathVariable Integer id) {
        return doctorService.findDoctorById(id);
    }

    @PutMapping
    public ResponseEntity<ResponseStructure<Doctor>> updateDoctor(@RequestBody Doctor doctor) {
        return doctorService.updateDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteDoctorById(@PathVariable Integer id) {
        return doctorService.deleteDoctorById(id);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<ResponseStructure<Set<Doctor>>> findByPatientId(@PathVariable Integer id) {
        return doctorService.findByPatientId(id);
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<ResponseStructure<Doctor>> findByAppointmentId(@PathVariable Integer id) {
        return doctorService.findByAppointmentId(id);
    }

}
