package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Dto.EmployeeDto;
import com.industry.company.Company_service.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> AddEmployee(@RequestBody EmployeeDto employeeDto)
    {
        EmployeeDto employeedto = employeeService.AddEmployee(employeeDto);

        return new ResponseEntity<>(employeedto , HttpStatus.CREATED);
    }

}
