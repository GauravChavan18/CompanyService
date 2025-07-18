package com.industry.company.Company_service.Controller;


import com.industry.company.Company_service.Entity.LeaveRequest;
import com.industry.company.Company_service.Service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaverequest")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveService leaveService;

    @PostMapping("/{employeeId}/{month}")
    private ResponseEntity<LeaveRequest> AddLeaveRequest(@PathVariable Long employeeId , @RequestBody LeaveRequest leaveRequest , @PathVariable String month)
    {
        LeaveRequest leaveRequest1 = leaveService.AddLeaveRequest(employeeId , leaveRequest,month);

        return new ResponseEntity<>(leaveRequest1 , HttpStatus.CREATED);
    }
}
