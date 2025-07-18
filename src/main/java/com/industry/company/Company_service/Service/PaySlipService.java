package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Entity.Earnings;
import com.industry.company.Company_service.Entity.PaySlip;

import java.io.OutputStream;

public interface PaySlipService {

    PaySlip CreatePaySlip(Long id , Earnings earnings);

    PaySlip CreatePaySlipByMonth(Long id , String Month ,Earnings earnings);


    void getPaySlipPdfByEmployeeIdAndMonth(Long id , String PayMonth , OutputStream outputStream);
}
