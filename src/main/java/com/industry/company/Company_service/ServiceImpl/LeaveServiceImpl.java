package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Entity.LeaveRequest;
import com.industry.company.Company_service.Entity.LeaveRequestStatus;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.LeaveRepository;
import com.industry.company.Company_service.Service.LeaveService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;

    private final EmploeeRepository emploeeRepository;

    @Override
    public LeaveRequest AddLeaveRequest(Long employeeId, LeaveRequest leaveRequest, String month) {

        EmployeeEntity employee = emploeeRepository
                .findById(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee Not Found"));

        leaveRequest.setLeaveRequestStatus(LeaveRequestStatus.SUBMITTED);
        leaveRequest.setEmployee(employee);
        leaveRepository.save(leaveRequest);


        return leaveRequest;
    }
}
