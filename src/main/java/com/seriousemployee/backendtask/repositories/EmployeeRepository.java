package com.seriousemployee.backendtask.repositories;

import com.seriousemployee.backendtask.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String username);
}
