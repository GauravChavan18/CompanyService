package com.industry.company.Company_service.Entity;


import jakarta.persistence.Embeddable;
import lombok.Data;


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

    public void calculateTotals() {
        totalEarningsMaster = baseSalary + hra + flexiPay + bonus + variablePay;
        totalEarningsAdj = shiftAllowance;
        totalEarningsPaid = totalEarningsMaster + totalEarningsAdj;
        grossEarnings = totalEarningsPaid;
    }

}
