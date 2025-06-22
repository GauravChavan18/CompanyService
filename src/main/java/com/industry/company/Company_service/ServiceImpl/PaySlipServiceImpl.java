package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Entity.Earnings;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Entity.PaySlip;
import com.industry.company.Company_service.Entity.TotalDeductions;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.PaySlipRepository;
import com.industry.company.Company_service.Service.PaySlipService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.OutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaySlipServiceImpl implements PaySlipService {

    private final EmploeeRepository emploeeRepository;

    private final PaySlipRepository paySlipRepository;

    private final SpringTemplateEngine templateEngine;

    @Override
    public void getPayslipPdf(Long id , OutputStream outputStream) {

        PaySlip paySlip = paySlipRepository.findByEmployeeEmployeeId(id)
                                .orElseThrow(()-> new ResourceNotFoundException("PaySlip not found "));

        Context context = new Context();
        context.setVariable("data", paySlip);
        String html = templateEngine.process("payslip", context);
        log.info("Rendered HTML:\n{}", html);

        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }

    }

    @Override
    public PaySlip CreatePaySlip(Long id , Earnings earnings) {
        EmployeeEntity employee = emploeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));


        PaySlip paySlip=new PaySlip();
        paySlip.setEmployee(employee);


        Earnings earningsTemp = new Earnings();

        earningsTemp.setBaseSalary(earnings.getBaseSalary());

        earningsTemp.setHra(15000);
        earningsTemp.setFlexiPay(3000);
        earningsTemp.setBonus(4000);
        earningsTemp.setVariablePay(2000);

        earningsTemp.setShiftAllowance(earnings.getShiftAllowance());

        earningsTemp.calculateTotals();



        TotalDeductions deductions = new TotalDeductions();
        deductions.setPfDeducted(1500);
        deductions.setProfTax(250);
        deductions.setAwtDeduction(5);
        deductions.calculateTotals();
        paySlip.setTotalDeductions(deductions);




        earningsTemp.setGrossEarnings(earningsTemp.getGrossEarnings()-deductions.getTotalDeductions());
        paySlip.setEarnings(earningsTemp);

        paySlipRepository.save(paySlip);
        return paySlip;
    }
}
