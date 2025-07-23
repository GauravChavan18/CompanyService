package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Entity.LeaveRequest;

import java.util.List;

public interface LeaveService {

    LeaveRequest AddLeaveRequest(Long employeeId , LeaveRequest leaveRequest , String month);

    List<LeaveRequest> GetLeaveRequest(Long employeeId);

    void UpdateLeaveRequestStatus(Long employeeId);
}
