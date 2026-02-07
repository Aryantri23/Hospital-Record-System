package com.main.hospitalrecordsystem.department.repository;

import com.main.hospitalrecordsystem.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByName(String name);
}
