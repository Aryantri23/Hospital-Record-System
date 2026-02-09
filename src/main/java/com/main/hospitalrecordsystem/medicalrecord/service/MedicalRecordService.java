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

import java.time.LocalDate;
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

        if (medicalRecord.getPrescription() != null) {
            throw new BadRequestException("Prescription Cannot be Saved Before medical Record");
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

    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findAllMedicalRecord() {

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        if (medicalRecords.isEmpty()) {
            throw new NoRecordFoundException("No Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<MedicalRecord>>()
                .setData(medicalRecords)
                .setMessage("All Medical Record")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<MedicalRecord>> findById(Integer id) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("Id Is Invalid"));

        return new ResponseEntity<>(new ResponseStructure<MedicalRecord>()
                .setData(medicalRecord)
                .setMessage("Medical Record Data Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByPatientId(Integer id) {

        patientRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Patient Id"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatient_Id(id);

        if (medicalRecords.isEmpty()) {
            throw new NoRecordFoundException("No Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<MedicalRecord>>()
                .setData(medicalRecords)
                .setMessage("All Medical Record For Patient Id " + id)
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByDoctorId(Integer id) {

        doctorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Doctor Id"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByDoctor_Id(id);

        if (medicalRecords.isEmpty()) {
            throw new NoRecordFoundException("No Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<MedicalRecord>>()
                .setData(medicalRecords)
                .setMessage("All Medical Record For Doctor Id " + id)
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<MedicalRecord>> findByAppointmentId(Integer id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Appointment id"));

        if (appointment.getStatus() != Status.COMPLETED) {
            throw new BadRequestException("Appointment Is Not Yet Completed");
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findByPatient_IdAndVisitDate(appointment.getPatient().getId(), appointment.getLocalDateTime().toLocalDate())
                .orElseThrow(() -> new NoRecordFoundException("No Record Found"));

        return new ResponseEntity<>(new ResponseStructure<MedicalRecord>()
                .setData(medicalRecord)
                .setMessage("Medical Record For Appointment Id " + id)
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByDate(LocalDate date) {

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByVisitDate(date);

        if (medicalRecords.isEmpty()) {
            throw new NoRecordFoundException("No Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<MedicalRecord>>()
                .setData(medicalRecords)
                .setMessage("All Medical Record For Visit Date : " + date)
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }
}
