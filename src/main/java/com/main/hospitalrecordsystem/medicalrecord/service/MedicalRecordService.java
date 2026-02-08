package com.main.hospitalrecordsystem.medicalrecord.service;

import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.appointment.repository.AppointmentRepository;
import com.main.hospitalrecordsystem.doctor.entity.Doctor;
import com.main.hospitalrecordsystem.doctor.repository.DoctorRepository;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.enums.Status;
import com.main.hospitalrecordsystem.exception.BadRequestException;
import com.main.hospitalrecordsystem.exception.IdProvidedException;
import com.main.hospitalrecordsystem.exception.NoRecordFoundException;
import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import com.main.hospitalrecordsystem.medicalrecord.repository.MedicalRecordRepository;
import com.main.hospitalrecordsystem.patient.entity.Patient;
import com.main.hospitalrecordsystem.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public MedicalRecordService(AppointmentRepository appointmentRepository, MedicalRecordRepository medicalRecordRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<ResponseStructure<MedicalRecord>> saveRecord(MedicalRecord medicalRecord) {

        if (medicalRecord.getId() != null) {
            throw new IdProvidedException("Id Is Auto Generated");
        }

        if (medicalRecord.getDiagnosis() == null) {
            throw new BadRequestException("Please Provide Diagnosis");
        }

        if (medicalRecord.getTreatment() == null) {
            throw new BadRequestException("Please Provide Treatment Details");
        }

        if (medicalRecord.getPatient() == null) {
            throw new BadRequestException("Please Provide Patient Id");
        }

        if (medicalRecord.getPatient().getId() == null) {
            throw new BadRequestException("Please Provide Patient Id");
        }

        Patient patient = patientRepository.findById(medicalRecord.getPatient().getId())
                .orElseThrow(() -> new BadRequestException("Invalid Patient Id"));

        List<Appointment> appointments = appointmentRepository.findByPatient_Id(patient.getId());

        if (appointments.isEmpty()) {
            throw new BadRequestException("No Appointment Record Found");
        }

        if (medicalRecord.getVisitDate() == null) {
            throw new BadRequestException("Please Provide Visit Date");
        }

        Appointment appointment = null;

        for (Appointment appointment1 : appointments) {
            if (appointment1.getLocalDateTime().toLocalDate().equals(medicalRecord.getVisitDate())) {
                appointment = appointment1;
                break;
            }
        }

        if (appointment == null) {
            throw new BadRequestException("Please Provide Correct Visit Date");
        }

        if (appointment.getStatus() != Status.COMPLETED) {
            throw new BadRequestException("Appointment Not Completed");
        }

        if (medicalRecord.getDoctor() != null) {
            throw new BadRequestException("Dont Pass Doctor Info");
        }

        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                        .orElseThrow(() -> new NoRecordFoundException("Doctor Info Not Found"));

        medicalRecord.setDoctor(doctor);
        medicalRecord.setPatient(patient);

        return new ResponseEntity<>(new ResponseStructure<MedicalRecord>()
                .setData(medicalRecordRepository.save(medicalRecord))
                .setMessage("Medical Record Saved")
                .setStatus(HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }
}
