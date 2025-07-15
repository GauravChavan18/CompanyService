package com.industry.company.Company_service.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PaySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PayslipId;


    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private EmployeeEntity employee;

    @Embedded
    private Earnings earnings;

    @Embedded
    private TotalDeductions totalDeductions;


}
