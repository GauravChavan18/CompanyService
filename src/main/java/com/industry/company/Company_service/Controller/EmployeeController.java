package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Dto.EmployeeDto;
import com.industry.company.Company_service.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> AddEmployee(@RequestBody EmployeeDto employeeDto, Principal principal) {
        log.info("AddEmployee request received. Admin: {}, Employee Email: {}", principal.getName(), employeeDto.getEmail());

        EmployeeDto employeedto = employeeService.AddEmployee(employeeDto, principal.getName());

        log.info("AddEmployee request completed successfully for Employee Email: {}", employeedto.getEmail());
        return new ResponseEntity<>(employeedto, HttpStatus.CREATED);
    }

    @GetMapping("/{companyName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto>> GetEmployeesByCompanyName(@PathVariable String companyName) {
        log.info("GetEmployeesByCompanyName request received for company: {}", companyName);

        List<EmployeeDto> employees = employeeService.GetEmployeesByCompanyName(companyName);

        log.info("Found {} employees for company: {}", employees.size(), companyName);
        return new ResponseEntity<>(employees, HttpStatus.FOUND);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto>> GetEmployeesByAdminName(Principal principal) {
        log.info("GetEmployeesByAdminName request received for admin: {}", principal.getName());

        List<EmployeeDto> employees = employeeService.GetEmployeesByAdminName(principal.getName());

        log.info("Found {} employees for admin: {}", employees.size(), principal.getName());
        return new ResponseEntity<>(employees, HttpStatus.FOUND);
    }
}
