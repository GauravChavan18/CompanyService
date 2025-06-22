package com.industry.company.Company_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PaySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PayslipId;


    @OneToOne
    @JoinColumn(name = "employeeId", nullable = false, unique = true)
    private EmployeeEntity employee;

    @Embedded
    private Earnings earnings;

    @Embedded
    private TotalDeductions totalDeductions;


}
