package com.industry.company.Company_service.Controller;


import com.industry.company.Company_service.Entity.LeaveRequest;
import com.industry.company.Company_service.Service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaverequest")
@RequiredArgsConstructor
@EnableMethodSecurity
public class LeaveRequestController {

    private final LeaveService leaveService;

    @PostMapping("/{employeeId}/{month}")
    @PreAuthorize("hasRole('USER')")
    private ResponseEntity<LeaveRequest> AddLeaveRequest(@PathVariable Long employeeId , @RequestBody LeaveRequest leaveRequest , @PathVariable String month)
    {
        LeaveRequest leaveRequest1 = leaveService.AddLeaveRequest(employeeId , leaveRequest,month);

        return new ResponseEntity<>(leaveRequest1 , HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    private ResponseEntity<List<LeaveRequest>> GetLeaveRequest(@PathVariable Long employeeId)
    {
        List<LeaveRequest> leaveRequests = leaveService.GetLeaveRequest(employeeId);

        return new ResponseEntity<>(leaveRequests, HttpStatus.FOUND);
    }


}
