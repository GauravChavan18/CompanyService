package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Dto.EmployeeDto;

import java.util.List;


public interface EmployeeService {

    EmployeeDto AddEmployee(EmployeeDto employeeDto);

    List<EmployeeDto> GetEmployeesByCompanyName(String companyName);
}
