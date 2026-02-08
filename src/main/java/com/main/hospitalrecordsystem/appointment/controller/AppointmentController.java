package com.main.hospitalrecordsystem.appointment.controller;

import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.appointment.service.AppointmentService;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
