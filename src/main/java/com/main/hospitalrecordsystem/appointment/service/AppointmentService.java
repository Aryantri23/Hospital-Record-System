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

import java.time.LocalDate;
import java.util.ArrayList;
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

    public ResponseEntity<ResponseStructure<Appointment>> findAppointmentById(Integer id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Appointment Found"));

        return new ResponseEntity<>(new ResponseStructure<Appointment>()
                .setData(appointment)
                .setMessage("Appointment Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByDate(LocalDate date) {

        List<Appointment> appointments = appointmentRepository.findAll();

        if (appointments.isEmpty()) {
            throw new NoRecordFoundException("No Appointment Found");
        }

        List<Appointment> appointmentList = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getLocalDateTime().toLocalDate().equals(date)) {
                appointmentList.add(appointment);
            }
        }

        if (appointmentList.isEmpty()) {
            throw new NoRecordFoundException("No Appointment Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Appointment>>()
                .setData(appointmentList)
                .setMessage("All Appointment Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByDoctorId(Integer id) {

        List<Appointment> appointments = appointmentRepository.findByDoctor_Id(id);

        if (appointments.isEmpty()) {
            throw new NoRecordFoundException("No Appointment Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Appointment>>()
                .setData(appointments)
                .setMessage("All Appointment Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByPatientId(Integer id) {

        List<Appointment> appointments = appointmentRepository.findByPatient_Id(id);

        if (appointments.isEmpty()) {
            throw new NoRecordFoundException("No Appointment Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Appointment>>()
                .setData(appointments)
                .setMessage("All Appointment Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Appointment>> cancelAppointment(Integer id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Appointment Found"));

        appointment.setStatus(Status.CANCELLED);

        return new ResponseEntity<>(new ResponseStructure<Appointment>()
                .setData(appointmentRepository.save(appointment))
                .setMessage("Appointment CANCELLED")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Appointment>> updateStatus(Integer id, Status status) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Appointment Found"));

        if (appointment.getStatus().equals(status)) {
            throw new BadRequestException("Same Status Cannot be Updated");
        }

        appointment.setStatus(status);

        return new ResponseEntity<>(new ResponseStructure<Appointment>()
                .setData(appointmentRepository.save(appointment))
                .setMessage("Appointment Status Changed")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Appointment>>> findAppointmentByStatus(Status status) {

        List<Appointment> appointments = appointmentRepository.findByStatus(status);

        if (appointments.isEmpty()) {
            throw new NoRecordFoundException("No Appointment Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Appointment>>()
                .setData(appointments)
                .setMessage("All Appointment Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

}
