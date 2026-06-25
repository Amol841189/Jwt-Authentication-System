package com.authentication.jwt.controller;

import com.authentication.jwt.entity.Employee;
import com.authentication.jwt.repository.EmployeeRepository;
import com.authentication.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@Tag(name = "Employee API", description = "Employee Management APIs")
public class EmployeeController{

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;

    public EmployeeController(EmployeeRepository employeeRepository, JwtUtil jwtUtil){
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }

    // Jwt token validation Method For Re-use 
    private boolean isValidToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);

        return jwtUtil.validateToken(token);
    }

    @Operation(
            summary = "Add Employee",
            description = "Creates a new employee. Requires JWT token in Authorization header."
    )
    @PostMapping("/add")
    public ResponseEntity<?> saveEmployee(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Employee employee) {

        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid or missing token"
                    ));
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @Operation(
        summary = "Get Employees By Salary",
        description = "Returns employees matching the given salary."
    )
    @GetMapping("/salary/{salary}")
    public ResponseEntity<?> getEmployeesSalary(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Double salary) {

        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid or missing token"
                    ));
        }

        return ResponseEntity.ok(
                employeeRepository.findBySalary(salary)
        );
    }

    @Operation(
        summary = "Get Employees With Salary Greater Than",
        description = "Returns employees whose salary is greater than the given amount."
    )
    @GetMapping("/salary-greater/{salary}")
    public ResponseEntity<?> getEmployeesBySalaryGreaterThan(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Double salary) {

        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid or missing token"
            ));
        }

        return ResponseEntity.ok(
                employeeRepository.findBySalaryGreaterThan(salary)
        );
    }

    @Operation(
        summary = "Delete Employee",
        description = "Deletes an employee by ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid or missing token"
            ));
        }

        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Employee not found"
            ));
        }

        employeeRepository.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Employee deleted successfully"
        ));
    }

    @Operation(
        summary = "Delete All Employees",
        description = "Deletes all employee records."
    )
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllEmployees(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid or missing token"
            ));
        }

        long count = employeeRepository.count();

        if (count == 0) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "No employees found"
            ));
        }

        employeeRepository.deleteAll();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "deletedRecords", count,
                "message", "All employees deleted successfully"
        ));
    }
}

