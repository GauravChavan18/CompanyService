package com.industry.company.Company_service.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    private String payMonth;

    private LocalDate todayDate;

    private LocalTime startTime;
    private LocalTime EndTime;

    private Double overtimeHours;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private EmployeeEntity employee;
}
