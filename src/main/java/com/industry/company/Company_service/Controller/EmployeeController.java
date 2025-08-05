package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Dto.EmployeeDto;
import com.industry.company.Company_service.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@EnableMethodSecurity
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/{admin}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> AddEmployee(@RequestBody EmployeeDto employeeDto , @PathVariable String admin)
    {
        EmployeeDto employeedto = employeeService.AddEmployee(employeeDto , admin);
        return new ResponseEntity<>(employeedto , HttpStatus.CREATED);
    }

    @GetMapping("/{companyName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto>> GetEmployeesByCompanyName(@PathVariable String companyName)
    {
        return new ResponseEntity<>(employeeService.GetEmployeesByCompanyName(companyName), HttpStatus.FOUND);
    }

}
