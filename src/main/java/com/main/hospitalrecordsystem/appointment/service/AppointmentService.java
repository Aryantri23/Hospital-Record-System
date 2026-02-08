package com.main.hospitalrecordsystem.appointment.service;

import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.appointment.repository.AppointmentRepository;
import com.main.hospitalrecordsystem.doctor.entity.Doctor;
import com.main.hospitalrecordsystem.doctor.repository.DoctorRepository;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.enums.Days;
import com.main.hospitalrecordsystem.enums.Status;
import com.main.hospitalrecordsystem.exception.BadRequestException;
import com.main.hospitalrecordsystem.exception.IdProvidedException;
import com.main.hospitalrecordsystem.exception.NoRecordFoundException;
import com.main.hospitalrecordsystem.patient.entity.Patient;
import com.main.hospitalrecordsystem.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(Appointment appointment) {

        if (appointment.getId() != null) {
            throw new IdProvidedException("Id Is Auto Generated");
        }

        if (appointment.getLocalDateTime() == null) {
            throw new BadRequestException("Date And Time Must Be Provided");
        }

        if (appointment.getStatus() != null) {
            throw new BadRequestException("Status Cannot Be Passed");
        }

        if (appointment.getDoctor() == null) {
            throw new BadRequestException("Doctor Id Must Be Passed");
        }

        if (appointment.getPatient() == null) {
            throw new BadRequestException("Patient Id Must Be Passed");
        }

        if (appointment.getPatient().getId() == null) {
            throw new BadRequestException("Patient Id Must Be Passed");
        }

        if (appointment.getDoctor().getId() == null) {
            throw new BadRequestException("Doctor Id Must Be Passed");
        }

        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new BadRequestException("Invalid Doctor Id"));

        List<Appointment> appointmentList1 = appointmentRepository.findByDoctor_Id(appointment.getDoctor().getId());

        if (!appointmentList1.isEmpty()) {
            for (Appointment appointment1 : appointmentList1) {
                if (appointment.getLocalDateTime().equals(appointment1.getLocalDateTime())) {
                    throw new BadRequestException("Doctor Have Another Appointment At That Time");
                }
            }
        }

        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new BadRequestException("Invalid Patient Id"));

        List<Appointment> appointmentList2 = appointmentRepository.findByPatient_Id(appointment.getPatient().getId());

        if (!appointmentList2.isEmpty()) {
            for (Appointment appointment2 : appointmentList2) {
                if (appointment.getLocalDateTime().toLocalDate().equals(appointment2.getLocalDateTime().toLocalDate())) {
                    throw new BadRequestException("Patient Cannot Have Two Appointment Same Day");
                }
            }
        }

        Days appointmentDay = Days.valueOf(appointment.getLocalDateTime()
                        .getDayOfWeek()
                        .name());

        if (!doctor.getAvailableDays().contains(appointmentDay)) {
            throw new BadRequestException("Doctor Is Not Available At This Date");
        }

        appointment.setDoctor(doctor);

        appointment.setPatient(patient);

        appointment.setStatus(Status.BOOKED);

        return new ResponseEntity<>(new ResponseStructure<Appointment>()
                .setData(appointmentRepository.save(appointment))
                .setMessage("Saved")
                .setStatus(HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<ResponseStructure<List<Appointment>>> findAllAppointment() {

        List<Appointment> appointments = appointmentRepository.findAll();

        if (appointments.isEmpty()) {
            throw new NoRecordFoundException("No Appointment Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Appointment>>()
                .setData(appointments)
                .setMessage("All Appointment Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

}
