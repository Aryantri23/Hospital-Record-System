package com.main.hospitalrecordsystem.patient.service;

import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.appointment.repository.AppointmentRepository;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.exception.*;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public PatientService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public ResponseEntity<ResponseStructure<Patient>> savePatient(Patient patient) {

        if (patient.getId() != null) {
            throw new IdProvidedException("Id Is Auto Generated");
        }

        if (patient.getName() == null) {
            throw new BadRequestException("Please Provide Name");
        }

        if (patient.getAge() == null) {
            throw new BadRequestException("Please Provide Age");
        }

        if (patient.getGender() == null) {
            throw new BadRequestException("Please Provide Gender");
        }

        if (patient.getPhone() == null) {
            throw new BadRequestException("Please Provide Phone Number");
        }

        if (patient.getPhone().length() != 10) {
            throw new BadRequestException("Invalid Phone Number");
        }

        for (char ch : patient.getPhone().toCharArray()) {
            if (!Character.isDigit(ch)) {
                throw new BadRequestException("Invalid Phone Number");
            }
        }

        if (patientRepository.findByPhone(patient.getPhone()).isPresent()) {
            throw new AlreadyExistException("Phone Number Already Exist");
        }

        if (patient.getEmail() == null) {
            throw new BadRequestException("Please Provide Email");
        }

        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new AlreadyExistException("Email Already Exist");
        }

        return new ResponseEntity<>(new ResponseStructure<Patient>()
                .setData(patientRepository.save(patient))
                .setMessage("Saved")
                .setStatus(HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<ResponseStructure<List<Patient>>> findAllPatient() {

        List<Patient> patients = patientRepository.findAll();

        if (patients.isEmpty()) {
            throw new NoRecordFoundException("No Patient Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Patient>>()
                .setData(patients)
                .setMessage("All Patient Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Patient>> findPatientById(Integer id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Record Found"));

        return new ResponseEntity<>(new ResponseStructure<Patient>()
                .setData(patient)
                .setMessage("Success")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Patient>> findPatientByPhone(String phone) {

        if (phone.length() != 10) {
            throw new BadRequestException("Invalid Phone Number");
        }

        for (char ch : phone.toCharArray()) {
            if (!Character.isDigit(ch)) {
                throw new BadRequestException("Invalid Phone Number");
            }
        }

        Patient patient = patientRepository.findByPhone(phone)
                .orElseThrow(() -> new NoRecordFoundException("No Record Found"));

        return new ResponseEntity<>(new ResponseStructure<Patient>()
                .setData(patient)
                .setMessage("Success")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Patient>>> findByAgeGreater(Integer age) {

        List<Patient> patients = patientRepository.findByAgeGreaterThan(age);

        if (patients.isEmpty()) {
            throw new NoRecordFoundException("No Record Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Patient>>()
                .setData(patients)
                .setMessage("Success")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Patient>> updatePatient(Patient patient) {

        if (patient.getId() == null) {
            throw new IdNotProvidedException("Please Provide Id");
        }

        Patient patient1 = patientRepository.findById(patient.getId())
                .orElseThrow(() -> new NoRecordFoundException("No Record Found For Update"));

        if (patient.getEmail() != null && !patient.getEmail().equals(patient1.getEmail()) && patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new BadRequestException("Email Already Registered");
        }

        if (patient.getPhone() != null && !patient.getPhone().equals(patient1.getPhone()) && patientRepository.findByPhone(patient.getPhone()).isPresent()) {
            throw new BadRequestException("Phone Number Already Registered");
        }

        if (patient.getAge() != null && patient.getAge() <= 0) {
            throw new BadRequestException("Invalid Age");
        }

        if (patient.getPhone() != null && patient.getPhone().length() != 10) {
            throw new BadRequestException("Invalid Phone Number");
        }

        if (patient.getPhone() != null && patient.getPhone().length() == 10) {

            for (char ch : patient.getPhone().toCharArray()) {
                if (!Character.isDigit(ch)) {
                    throw new BadRequestException("Invalid Phone Number");
                }
            }

            patient1.setPhone(patient.getPhone());
        }

        if (patient.getName() != null) {
            patient1.setName(patient.getName());
        }

        if (patient.getEmail() != null) {
            patient1.setEmail(patient.getEmail());
        }

        if (patient.getAge() != null) {
            patient1.setAge(patient.getAge());
        }

        if (patient.getGender() != null) {
            patient1.setGender(patient.getGender());
        }

        return new ResponseEntity<>(new ResponseStructure<Patient>()
                .setData(patientRepository.save(patient1))
                .setMessage("Updated")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<String>> deletePatient(Integer id) {

        patientRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Record Exist With This Id"));

        patientRepository.deleteById(id);

        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Deleted")
                .setMessage("Patient Is Deleted")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Patient>> findByAppointmentId(Integer id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Appointment Id"));

        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new NoRecordFoundException("No Record Found"));

        return new ResponseEntity<>(new ResponseStructure<Patient>()
                .setData(patient)
                .setMessage("Patient Is Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );

    }

    public ResponseEntity<ResponseStructure<Patient>> findByRecordId(Integer id) {

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid MedicalRecord Id"));

        Patient patient = patientRepository.findById(record.getPatient().getId())
                .orElseThrow(() -> new NoRecordFoundException("No Record Found"));

        return new ResponseEntity<>(new ResponseStructure<Patient>()
                .setData(patient)
                .setMessage("Patient Is Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }
}
