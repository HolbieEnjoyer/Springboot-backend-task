package com.seriousemployee.backendtask.repositories;

import com.seriousemployee.backendtask.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
