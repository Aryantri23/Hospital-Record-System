package com.main.hospitalrecordsystem.doctor.service;

import com.main.hospitalrecordsystem.department.entity.Department;
import com.main.hospitalrecordsystem.department.repository.DepartmentRepository;
import com.main.hospitalrecordsystem.doctor.entity.Doctor;
import com.main.hospitalrecordsystem.doctor.repository.DoctorRepository;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.enums.Days;
import com.main.hospitalrecordsystem.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DoctorService(DepartmentRepository departmentRepository, DoctorRepository doctorRepository) {
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public ResponseEntity<ResponseStructure<Doctor>> saveDoctor(Doctor doctor) {

        if (doctor.getId() != null) {
            throw new IdProvidedException("Id is Auto Generated");
        }

        if (doctor.getName() == null) {
            throw new BadRequestException("Name Must Be Provided");
        }

        if (doctor.getDepartment() == null) {
            throw new BadRequestException("Department Id Must Be Provided");
        }

        if (doctor.getDepartment().getId() == null) {
            throw new BadRequestException("Department Id Must Be Provided");
        }

        departmentRepository.findById(doctor.getDepartment().getId())
                .orElseThrow(() -> new NoRecordFoundException("Department Id Is Invalid"));


        if (doctor.getSpecialization() == null) {
            throw new BadRequestException("Specialization Must Be Provided");
        }

        if (doctor.getAvailableDays() == null) {
            throw new BadRequestException("AvailableDays Must Be Provided");
        }

        if (doctor.getAvailableDays().isEmpty()) {
            throw new BadRequestException("At least One Available Day Should Be Provided");
        }

        return new ResponseEntity<>(new ResponseStructure<Doctor>()
                .setData(doctorRepository.save(doctor))
                .setMessage("Doctor Is Saved")
                .setStatus(HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );

    }

    public ResponseEntity<ResponseStructure<List<Doctor>>> findAllDoctor() {

        List<Doctor> doctors = doctorRepository.findAll();

        if (doctors.isEmpty()) {
            throw new NoRecordFoundException("No Doctor Records Are Available");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Doctor>>()
                .setData(doctors)
                .setStatus(HttpStatus.OK.value())
                .setMessage("Doctor Records Retrieved"),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Doctor>>> findDoctorBySpecialization(String specialization) {

        if (specialization == null) {
            throw new BadRequestException("Please Provide Specialization");
        }

        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);

        if (doctors.isEmpty()) {
            throw new NoRecordFoundException("No Record Found Try With Different Specialization");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Doctor>>()
                .setData(doctors)
                .setStatus(HttpStatus.OK.value())
                .setMessage("Doctor Records Retrieved"),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Doctor>>> findByDepartment(String department) {

        Department department1 = departmentRepository.findByName(department)
                .orElseThrow(() -> new DepartmentNameNotFoundException("No Department Exist"));

        List<Doctor> doctors = doctorRepository.findByDepartment_Id(department1.getId());

        if (doctors.isEmpty()) {
            throw new NoRecordFoundException("No Doctor Found In This Department");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Doctor>>()
                .setData(doctors)
                .setMessage("Doctor Records Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Set<Doctor>>> findDoctorByAvailableDays(Set<Days> days) {

        if (days == null) {
            throw new BadRequestException("Please Provide At Least One Day");
        }

        if (days.isEmpty()) {
            throw new BadRequestException("Please Provide At Least One Day");
        }

        List<Doctor> doctors = doctorRepository.findAll();

        Set<Doctor> doctorList = new HashSet<>();

        for (Days day : days) {
            for (Doctor doctor : doctors) {
                if (doctor.getAvailableDays().contains(day)) {
                    doctorList.add(doctor);
                }
            }
        }

        if (doctorList.isEmpty()) {
            throw new NoRecordFoundException("No Doctor Available");
        }


        return new ResponseEntity<>(new ResponseStructure<Set<Doctor>>()
                .setData(doctorList)
                .setMessage("Doctor Records Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Doctor>> findDoctorById(Integer id) {

        if (id == null) {
            throw new BadRequestException("Please Pass Id");
        }

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Doctor Found Try With Different Id"));

        return new ResponseEntity<>(new ResponseStructure<Doctor>()
                .setData(doctor)
                .setMessage("Doctor Retrieved Successfully")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Doctor>> updateDoctor(Doctor doctor) {

        if (doctor.getId() == null) {
            throw new IdNotProvidedException("Please Provide Id");
        }

        Doctor doctor1 = doctorRepository.findById(doctor.getId())
                .orElseThrow(() -> new NoRecordFoundException("No Matched Record Found"));

        if (doctor.getDepartment() != null && doctor.getDepartment().getId() != null && departmentRepository.findById(doctor.getDepartment().getId()).isEmpty()) {
            throw new NoRecordFoundException("Department Not Present Try With Different Id");
        }

        if (doctor.getDepartment() != null && doctor.getDepartment().getId() != null) {
            doctor1.setDepartment(doctor.getDepartment());
        }

        if (doctor.getName() != null) {
            doctor1.setName(doctor.getName());
        }

        if (doctor.getAvailableDays() != null && !doctor.getAvailableDays().isEmpty()) {
            doctor1.setAvailableDays(doctor.getAvailableDays());
        }

        if (doctor.getSpecialization() != null) {
            doctor1.setSpecialization(doctor.getSpecialization());
        }

        return new ResponseEntity<>(new ResponseStructure<Doctor>()
                .setData(doctorRepository.save(doctor1))
                .setMessage("Updated")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<String>> deleteDoctorById(Integer id) {

        if (id == null) {
            throw new IdNotProvidedException("Id Must Be Provided");
        }

        doctorRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Matched Record Found"));

        doctorRepository.deleteById(id);

        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Deleted doctor with id : " + id)
                .setMessage("Deleted")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

}
