package com.industry.company.Company_service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LeaveBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LeaveBalanceId;

    private Long LeaveBalace=24L;

    @OneToOne
    @JoinColumn(name = "employeeId")
    private EmployeeEntity employee;
}
