package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.AuthEntity.AuthStatus;
import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
import com.industry.company.Company_service.AuthEntity.Roles;
import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Dto.EmployeeDto;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Entity.LeaveBalanceEntity;
import com.industry.company.Company_service.Repository.*;
import com.industry.company.Company_service.Service.EmployeeService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final EmploeeRepository emploeeRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeAuthEntityRepo employeeAuthEntityRepo;


    @Override
    public EmployeeDto AddEmployee(EmployeeDto employeeDto , String admin) {

        EmployeeEntity employee = modelMapper.map(employeeDto,EmployeeEntity.class);

        String companyName = employeeDto.getCompanyName();

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                        .orElseThrow(()-> new ResourceNotFoundException("Company not found"));

        AdminEntity adminEntity = adminRepository.findById(admin).orElseThrow(()-> new ResourceNotFoundException("Admin is not assigned"));

        LeaveBalanceEntity leaveBalanceEntity = new LeaveBalanceEntity();
        leaveBalanceEntity.setLeaveBalace(24L);
        leaveBalanceEntity.setEmployee(employee);

        employee.setCompany(company);
        employee.setAdminEntity(adminEntity);

        emploeeRepository.save(employee);


        leaveBalanceRepository.save(leaveBalanceEntity);

        setEmployeeAuthDetails(employee);
        return modelMapper.map(employee,EmployeeDto.class);
    }

    private void setEmployeeAuthDetails(EmployeeEntity employee) {
        EmployeeAuthEntity employeeAuth = new EmployeeAuthEntity();
        employeeAuth.setEmployee(employee);
        employeeAuth.setEmail(employee.getEmail());
        employeeAuth.setPassword(passwordEncoder.encode(employee.getEmployeeName()+"123"));
        employeeAuth.setRole(Roles.USER);
        employeeAuth.setStatus(AuthStatus.PENDING_PASSWORD);

        employeeAuthEntityRepo.save(employeeAuth);

    }

    @Override
    public List<EmployeeDto> GetEmployeesByCompanyName(String companyName) {

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found"));
        log.info("in get employess");
        List<EmployeeEntity> employeeEntities =emploeeRepository.findByCompanyCompanyName(companyName);

        List<EmployeeDto> employeeDtos = employeeEntities
                .stream()
                .map((employee -> modelMapper.map(employee ,EmployeeDto.class)))
                .toList();


        return employeeDtos;
    }


}
