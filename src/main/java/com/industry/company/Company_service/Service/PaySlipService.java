package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Entity.Earnings;
import com.industry.company.Company_service.Entity.PaySlip;

import java.io.OutputStream;
import java.util.List;

public interface PaySlipService {

    PaySlip CreatePaySlip(Long id , Earnings earnings , String email);

    PaySlip CreatePaySlipByMonth(Long id , String Month ,Earnings earnings , String email);

    List<PaySlip> getAllPlayslipsByEmployeeeId(Long id , String email);

    void getPaySlipPdfByEmployeeIdAndMonth(Long id , String PayMonth , OutputStream outputStream , String email);
}
