package com.industry.company.Company_service.Dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class EmployeeDto {

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
    private LocalDate start;
    private LocalDate end;
    private String payMonth;
    private double daysPayable;
    private String currency;

    private String companyName;
}
