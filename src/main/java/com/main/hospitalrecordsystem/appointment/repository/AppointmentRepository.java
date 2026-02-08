package com.main.hospitalrecordsystem.appointment.repository;

import com.main.hospitalrecordsystem.appointment.entity.Appointment;
import com.main.hospitalrecordsystem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByDoctor_Id(Integer id);

    List<Appointment> findByPatient_Id(Integer id);

    List<Appointment> findByStatus(Status status);
}
