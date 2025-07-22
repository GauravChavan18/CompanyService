package com.industry.company.Company_service.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;

@Entity
@Data
@RequiredArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LeaveRequestId;

    private String LeaveReason;

    private LocalDate LeaveStartDate;

    private LocalDate LeaveEndDate;

    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus leaveRequestStatus;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private EmployeeEntity employee;


}
