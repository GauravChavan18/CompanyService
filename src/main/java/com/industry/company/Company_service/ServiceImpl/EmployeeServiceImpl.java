package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Dto.EmployeeDto;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Entity.LeaveBalanceEntity;
import com.industry.company.Company_service.Repository.CompanyRepository;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.LeaveBalanceRepository;
import com.industry.company.Company_service.Repository.LeaveRepository;
import com.industry.company.Company_service.Service.EmployeeService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmploeeRepository emploeeRepository;

    private final CompanyRepository companyRepository;

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto AddEmployee(EmployeeDto employeeDto) {

        EmployeeEntity employee = modelMapper.map(employeeDto,EmployeeEntity.class);

        String companyName = employeeDto.getCompanyName();

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                        .orElseThrow(()-> new ResourceNotFoundException("Company not found"));

        LeaveBalanceEntity leaveBalanceEntity = new LeaveBalanceEntity();
        leaveBalanceEntity.setLeaveBalace(24L);
        leaveBalanceEntity.setEmployee(employee);

        employee.setCompany(company);

        emploeeRepository.save(employee);
        leaveBalanceRepository.save(leaveBalanceEntity);

        return modelMapper.map(employee,EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> GetEmployeesByCompanyName(String companyName) {

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found"));

        List<EmployeeEntity> employeeEntities =emploeeRepository.findByCompanyCompanyName(companyName);

        List<EmployeeDto> employeeDtos = employeeEntities
                .stream()
                .map((employee -> modelMapper.map(employee ,EmployeeDto.class)))
                .toList();


        return employeeDtos;
    }


}
