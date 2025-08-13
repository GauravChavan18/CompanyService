package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.AuthEntity.AuthStatus;
import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
import com.industry.company.Company_service.AuthEntity.Roles;
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

import java.util.List;

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
    public EmployeeDto AddEmployee(EmployeeDto employeeDto, String admin) {
        log.info("AddEmployee called with admin: {}, employeeDto: {}", admin, employeeDto);

        String companyName = employeeDto.getCompanyName();
        log.debug("Fetching company with name: {}", companyName);

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                .orElseThrow(() -> {
                    log.error("Company not found: {}", companyName);
                    return new ResourceNotFoundException("Company not found");
                });

        EmployeeEntity employee = modelMapper.map(employeeDto, EmployeeEntity.class);
        log.debug("Mapped EmployeeDto to EmployeeEntity: {}", employee);

        AdminEntity adminEntity = adminRepository.findById(admin)
                .orElseThrow(() -> {
                    log.error("Admin not assigned: {}", admin);
                    return new ResourceNotFoundException("Admin is not assigned");
                });

        LeaveBalanceEntity leaveBalanceEntity = new LeaveBalanceEntity();
        leaveBalanceEntity.setLeaveBalace(24L);
        leaveBalanceEntity.setEmployee(employee);

        employee.setCompany(company);
        employee.setAdminEntity(adminEntity);

        log.debug("Saving employee: {}", employee);
        emploeeRepository.save(employee);

        log.debug("Saving leave balance: {}", leaveBalanceEntity);
        leaveBalanceRepository.save(leaveBalanceEntity);

        setEmployeeAuthDetails(employee);

        log.info("Employee added successfully with email: {}", employee.getEmail());
        return modelMapper.map(employee, EmployeeDto.class);
    }

    private void setEmployeeAuthDetails(EmployeeEntity employee) {
        log.debug("Setting auth details for employee: {}", employee.getEmail());

        EmployeeAuthEntity employeeAuth = new EmployeeAuthEntity();
        employeeAuth.setEmployee(employee);
        employeeAuth.setEmail(employee.getEmail());
        employeeAuth.setPassword(passwordEncoder.encode(employee.getEmployeeName() + "123"));
        employeeAuth.setRole(Roles.USER);
        employeeAuth.setStatus(AuthStatus.PENDING_PASSWORD);

        employeeAuthEntityRepo.save(employeeAuth);
        log.info("Employee auth details saved for: {}", employee.getEmail());
    }

    @Override
    public List<EmployeeDto> GetEmployeesByCompanyName(String companyName) {
        log.info("Fetching employees for company: {}", companyName);

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                .orElseThrow(() -> {
                    log.error("Company not found: {}", companyName);
                    return new ResourceNotFoundException("Company not found");
                });

        List<EmployeeEntity> employeeEntities = emploeeRepository.findByCompanyCompanyName(companyName);
        log.debug("Found {} employees for company: {}", employeeEntities.size(), companyName);

        return employeeEntities.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public List<EmployeeDto> GetEmployeesByAdminName(String admin) {
        log.info("Fetching employees for admin: {}", admin);

        List<EmployeeEntity> employeeEntities = emploeeRepository.findByAdminEntityAdminEmail(admin);
        log.debug("Found {} employees for admin: {}", employeeEntities.size(), admin);

        if (employeeEntities.isEmpty()) {
            log.warn("No employees found for admin: {}", admin);
            throw new ResourceNotFoundException("Employees Not Assosiated till now");
        }

        return employeeEntities.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }
}
