package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Entity.Earnings;
import com.industry.company.Company_service.Entity.PaySlip;

import java.io.OutputStream;

public interface PaySlipService {

    void getPayslipPdf(Long id, OutputStream outputStream);

    PaySlip CreatePaySlip(Long id , Earnings earnings);
}
