package com.main.hospitalrecordsystem.appointment.controller;

import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.appointment.service.AppointmentService;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hospital/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(@RequestBody Appointment appointment) {
        return appointmentService.bookAppointment(appointment);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Appointment>>> findAllAppointment() {
        return appointmentService.findAllAppointment();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseStructure<Appointment>> findAppointmentById(@PathVariable Integer id) {
        return appointmentService.findAppointmentById(id);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByDate(@PathVariable LocalDate date) {
        return appointmentService.findAppointmentByDate(date);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByDoctorId(@PathVariable Integer id) {
        return appointmentService.findAppointmentByDoctorId(id);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByPatientId(@PathVariable Integer id) {
        return appointmentService.findAppointmentByPatientId(id);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ResponseStructure<Appointment>> cancelAppointment(@PathVariable Integer id) {
        return appointmentService.cancelAppointment(id);
    }

    @PutMapping("/id/{id}/status/{status}")
    public ResponseEntity<ResponseStructure<Appointment>> updateStatus(@PathVariable Integer id, @PathVariable Status status) {
        return appointmentService.updateStatus(id, status);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByStatus(@PathVariable Status status) {
        return appointmentService.findAppointmentByStatus(status);
    }
}
