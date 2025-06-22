package com.industry.company.Company_service.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class TotalDeductions {

    private double pfDeducted;
    private double profTax;
    private double awtDeduction;
    private double totalDeductions;


    public void calculateTotals() {
        totalDeductions = pfDeducted + profTax + awtDeduction;
    }
}
