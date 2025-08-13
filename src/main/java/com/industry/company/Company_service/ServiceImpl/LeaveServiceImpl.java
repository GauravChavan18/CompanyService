package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Entity.LeaveBalanceEntity;
import com.industry.company.Company_service.Entity.LeaveRequest;
import com.industry.company.Company_service.Entity.LeaveRequestStatus;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.LeaveBalanceRepository;
import com.industry.company.Company_service.Repository.LeaveRepository;
import com.industry.company.Company_service.Service.LeaveService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;

    private final EmploeeRepository emploeeRepository;

    private  final LeaveBalanceRepository leaveBalanceRepository;

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

    @Override
    public List<LeaveRequest> GetLeaveRequest(Long employeeId) {

        EmployeeEntity employee = emploeeRepository
                .findById(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee Not Found"));


        List<LeaveRequest> leaveRequests =
                leaveRepository
                        .findAllByEmployeeEmployeeId(employeeId);

        if (leaveRequests.isEmpty()) {
            throw new ResourceNotFoundException("Leave Not Found for this Employee");
        }

        return  leaveRequests;


    }

    @Override
    public void UpdateLeaveRequestStatus(Long employeeId) {

        EmployeeEntity employee = emploeeRepository
                .findById(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee Not Found"));

        LeaveBalanceEntity leavebalance = leaveBalanceRepository.findByEmployeeEmployeeId(employeeId);

        List<LeaveRequest> leaveRequests = leaveRepository.findAllByEmployeeEmployeeId(employeeId);

        leaveRequests.forEach(leaveRequest ->
        {
            if(LeaveRequestStatus.SUBMITTED.equals(leaveRequest.getLeaveRequestStatus()))
            {
                Period period = Period.between(leaveRequest.getLeaveStartDate(), leaveRequest.getLeaveEndDate());
                log.info(period.getDays()+"");
                leavebalance.setLeaveBalace(leavebalance.getLeaveBalace()- (period.getDays()+1));
                leaveBalanceRepository.save(leavebalance);
            }
            leaveRequest.setLeaveRequestStatus(LeaveRequestStatus.APPROVED);
            leaveRepository.save(leaveRequest);
        });

    }


}
