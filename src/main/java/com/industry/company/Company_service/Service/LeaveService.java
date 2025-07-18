package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Entity.LeaveRequest;

public interface LeaveService {

    LeaveRequest AddLeaveRequest(Long employeeId , LeaveRequest leaveRequest , String month);
}
