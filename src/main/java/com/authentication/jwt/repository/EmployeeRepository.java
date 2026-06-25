package com.authentication.jwt.repository;

import com.authentication.jwt.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

      List<Employee> findBySalary(Double salary);
      List<Employee> findBySalaryGreaterThan(Double salary);
}