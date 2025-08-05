package com.industry.company.Company_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Entity
@Data
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeName;

    private String email;

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

    @ManyToOne
    @JoinColumn(name = "companyName" , nullable=false)
    @JsonIgnore
    public CompanyEntity company;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PaySlip> payslip;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AttendanceRecord> attendanceRecords;

    @OneToMany(mappedBy = "employee" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LeaveRequest> leaveRequestList;

    @OneToOne(mappedBy = "employee")
    @JsonIgnore
    private LeaveBalanceEntity leaveBalanceEntity;

    @ManyToOne
    @JoinColumn(name = "admin" ,nullable = false)
    private AdminEntity adminEntity;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private EmployeeAuthEntity userAuth;
}
