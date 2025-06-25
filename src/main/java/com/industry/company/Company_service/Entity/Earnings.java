package com.industry.company.Company_service.Entity;


import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;


@Data
@Embeddable
public class Earnings {

    private double baseSalary;
    private double hra;
    private double flexiPay;
    private double shiftAllowance;
    private double bonus;
    private double variablePay;
    private double totalEarningsMaster;
    private double totalEarningsAdj;
    private double totalEarningsPaid;
    private double grossEarnings;

    private LocalDate startDate;
    private LocalDate endDate;
    private String payMonth;
    private double daysPayable;
    private String currency;



    public void calculateTotals() {
        totalEarningsMaster = baseSalary + hra + flexiPay + bonus + variablePay;
        totalEarningsAdj = shiftAllowance;
        totalEarningsPaid = totalEarningsMaster + totalEarningsAdj;
        grossEarnings = totalEarningsPaid;
    }

}
