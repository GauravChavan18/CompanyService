package com.industry.company.Company_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import java.time.format.TextStyle;
import java.util.Locale;

@Entity
@Data
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeName;

    private String pan;

    private String pfNumber;

    private String uanNumber;

    private String esiNumber;

    private String department;
    private String function;
    private String location;
    private String bank;
    private String bankAccount;

    private LocalDate hireDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String payMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    private double daysPayable;
    private String currency;

    @ManyToOne
    @JoinColumn(name = "companyName" , nullable=false)
    @JsonIgnore
    public CompanyEntity company;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private PaySlip payslip;


}
