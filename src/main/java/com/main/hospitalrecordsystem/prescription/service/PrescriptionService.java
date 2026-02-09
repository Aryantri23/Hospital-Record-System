package com.main.hospitalrecordsystem.prescription.service;

import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.exception.AlreadyExistException;
import com.main.hospitalrecordsystem.exception.BadRequestException;
import com.main.hospitalrecordsystem.exception.NoRecordFoundException;
import com.main.hospitalrecordsystem.medicalrecord.entity.MedicalRecord;
import com.main.hospitalrecordsystem.medicalrecord.repository.MedicalRecordRepository;
import com.main.hospitalrecordsystem.patient.repository.PatientRepository;
import com.main.hospitalrecordsystem.prescription.entity.Prescription;
import com.main.hospitalrecordsystem.prescription.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrescriptionService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public PrescriptionService(MedicalRecordRepository medicalRecordRepository, PrescriptionRepository prescriptionRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<ResponseStructure<Prescription>> savePrescription(Integer id, Prescription prescription) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Record Id"));

        if (medicalRecord.getPrescription() != null) {
            throw new AlreadyExistException("Medical Record Already Have Prescription");
        }

        if (prescription.getMedicine() == null) {
            throw new BadRequestException("Please Provide Medicine Details");
        }

        if (prescription.getDosage() == null) {
            throw new BadRequestException("Please Provide Dosage Details");
        }

        if (prescription.getInstruction() == null) {
            throw new BadRequestException("Please Provide Instruction Details");
        }

        medicalRecord.setPrescription(prescriptionRepository.save(prescription));

        return new ResponseEntity<>(new ResponseStructure<Prescription>()
                .setData(prescriptionRepository.findById(medicalRecordRepository.save(medicalRecord).getPrescription().getId()).get())
                .setMessage("Prescription Saved")
                .setStatus(HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<ResponseStructure<List<Prescription>>> findAllPrescription() {

        List<Prescription> prescriptions = prescriptionRepository.findAll();

        if (prescriptions.isEmpty()) {
            throw new NoRecordFoundException("No Prescription Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Prescription>>()
                .setData(prescriptions)
                .setMessage("All Prescription Records")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Prescription>> findById(Integer id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Prescription Found"));

        return new ResponseEntity<>(new ResponseStructure<Prescription>()
                .setData(prescription)
                .setMessage("Prescription Record Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Prescription>> findByRecordId(Integer id) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Record Id"));

        if (medicalRecord.getPrescription() == null) {
            throw new NoRecordFoundException("No Prescription Found");
        }

        Prescription prescription = prescriptionRepository.findById(medicalRecord.getPrescription().getId())
                .orElseThrow(() -> new NoRecordFoundException("No Prescription Found"));

        return new ResponseEntity<>(new ResponseStructure<Prescription>()
                .setData(prescription)
                .setMessage("Prescription Record Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Prescription>>> findByPatientId(Integer id) {

        patientRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invalid Patient Id"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatient_Id(id);

        if (medicalRecords.isEmpty()) {
            throw new NoRecordFoundException("No Prescription Found");
        }

        List<Prescription> prescriptions = new ArrayList<>();

        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getPrescription() != null) {
                prescriptions.add(medicalRecord.getPrescription());
            }
        }

        if (prescriptions.isEmpty()) {
            throw new NoRecordFoundException("No Prescription Found");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Prescription>>()
                .setData(prescriptions)
                .setMessage("All Prescription Details")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }
}
