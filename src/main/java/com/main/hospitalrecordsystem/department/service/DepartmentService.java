package com.main.hospitalrecordsystem.department.service;

import com.main.hospitalrecordsystem.department.entity.Department;
import com.main.hospitalrecordsystem.department.repository.DepartmentRepository;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import com.main.hospitalrecordsystem.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public ResponseEntity<ResponseStructure<Department>> saveDepartment(Department department) {

        if (department.getName() == null) {
            throw new DepartmentNameNotFoundException("Name Must Be Provided");
        }

        if (department.getId() != null) {
            throw new IdProvidedException("Id is Auto Generated");
        }

        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new AlreadyExistException("Department Name Already Exist");
        }

        return new ResponseEntity<>(new ResponseStructure<Department>()
                .setData(departmentRepository.save(department))
                .setMessage("Department Saved")
                .setStatus(HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<ResponseStructure<List<Department>>> findAll(int page, int size) {

        Page<Department> departmentPage = departmentRepository.findAll(PageRequest.of(page, size, Sort.by("id")));

        if (departmentPage.getTotalElements() == 0) {
            throw new NoRecordFoundException("No Department Record Exist");
        }

        List<Department> departments = departmentPage.getContent();

        if (departments.isEmpty()) {
            throw new NoRecordFoundException("No Records On This Page");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Department>>()
                .setData(departments)
                .setMessage("Departments Details")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<List<Department>>> findAll() {

        List<Department> departments = departmentRepository.findAll();

        if (departments.isEmpty()) {
            throw new NoRecordFoundException("No Department Record Exist");
        }

        return new ResponseEntity<>(new ResponseStructure<List<Department>>()
                .setData(departments)
                .setMessage("All Department Details")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Department>> findById(Integer id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("No Record Found With This Id"));

        return new ResponseEntity<>(new ResponseStructure<Department>()
                .setData(department)
                .setMessage("Department Retrieved")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Department>> updateDepartment(Department department) {

        if (department.getId() == null) {
            throw new IdNotProvidedException("Id Must Be Provided");
        }

        departmentRepository.findById(department.getId())
                .orElseThrow(() -> new NoRecordFoundException("No Record Found With This Id"));

        if (department.getName() == null) {
            throw new DepartmentNameNotFoundException("Department Name Must be Provided");
        }

        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new AlreadyExistException("Department Already Exist");
        }

        return new ResponseEntity<>(new ResponseStructure<Department>()
                .setData(departmentRepository.save(department))
                .setMessage("Department Updated")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Department>> deleteDepartment(Integer id) {

        Department department = departmentRepository.findById(id)
                        .orElseThrow(() -> new NoRecordFoundException("No Department Found For Deletion"));

        departmentRepository.deleteById(id);

        return new ResponseEntity<>(new ResponseStructure<Department>()
                .setData(department)
                .setMessage("Deleted Successfully")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResponseStructure<Department>> findByName(String name) {

        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new NoRecordFoundException("No Department Found Try With Name"));

        return new ResponseEntity<>(new ResponseStructure<Department>()
                .setData(department)
                .setMessage("Department Retrieved Successfully")
                .setStatus(HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

}
