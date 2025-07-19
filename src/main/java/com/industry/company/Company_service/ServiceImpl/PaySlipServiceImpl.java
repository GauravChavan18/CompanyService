package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Entity.*;
import com.industry.company.Company_service.Repository.AttendenceRepository;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.PaySlipRepository;
import com.industry.company.Company_service.Service.PaySlipService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaySlipServiceImpl implements PaySlipService {

    private final EmploeeRepository emploeeRepository;

    private final PaySlipRepository paySlipRepository;

    private final SpringTemplateEngine templateEngine;

    private final AttendenceRepository attendenceRepository;



    @Override
    public void getPaySlipPdfByEmployeeIdAndMonth(Long id, String PayMonth, OutputStream outputStream) {
        PaySlip paySlip = paySlipRepository.findByEmployeeEmployeeIdAndEarnings_PayMonth(id,PayMonth)
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
        earningsTemp.setPayMonth(earnings.getPayMonth());
        earningsTemp.setStartDate(earnings.getStartDate());
        earningsTemp.setEndDate(earnings.getEndDate());
        log.info(String.valueOf(earnings.getEndDate().getDayOfMonth()));
        earningsTemp.setDaysPayable(earnings.getEndDate().getDayOfMonth() - earnings.getStartDate().getDayOfMonth());
        earningsTemp.setPayMonth(earnings.getStartDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        earningsTemp.setCurrency("INR");
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

    @Override
    public PaySlip CreatePaySlipByMonth(Long employeeId, String PayMonth, Earnings earnings) {
        EmployeeEntity employee = emploeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));


        LocalDate StartDate = LocalDate.of(LocalDate.now().getYear(), java.time.Month.valueOf(PayMonth.toUpperCase()),1);
        LocalDate EndDate = StartDate.withDayOfMonth(StartDate.lengthOfMonth());
        List<AttendanceRecord> records =attendenceRepository.findByEmployeeIdAndTodayDateBetween(employeeId,StartDate,EndDate);

        Earnings earningsTemp = EarningsCalculation(earnings , records ,PayMonth);


        TotalDeductions deductions = new TotalDeductions();
        deductions.setPfDeducted(1500);
        deductions.setProfTax(250);
        deductions.setAwtDeduction(5);
        deductions.calculateTotals();

        earningsTemp.setGrossEarnings(earningsTemp.getGrossEarnings()-deductions.getTotalDeductions());



        PaySlip paySlip= paySlipRepository
                .findByEmployeeEmployeeIdAndEarnings_PayMonth(employeeId,PayMonth)
                .orElse(new PaySlip());

        paySlip.setEmployee(employee);
        paySlip.setEarnings(earningsTemp);
        paySlip.setTotalDeductions(deductions);

        paySlipRepository.save(paySlip);

        return paySlip;
    }

    @Override
    public List<PaySlip> getAllPlayslipsByEmployeeeId(Long employeeId) {
        EmployeeEntity employee = emploeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));

        List<PaySlip> paySlipList = paySlipRepository.findByEmployeeEmployeeId(employeeId);

        return paySlipList;

    }


    public Earnings EarningsCalculation(Earnings earnings , List<AttendanceRecord> records , String PayMonth)
    {
        double overtimeHrs =2.5;
        double overtimePay = overtimeHrs *100;

        LocalDate StartDate = LocalDate.of(LocalDate.now().getYear(), java.time.Month.valueOf(PayMonth.toUpperCase()),1);
        LocalDate EndDate = StartDate.withDayOfMonth(StartDate.lengthOfMonth());

        Earnings earningsTemp = new Earnings();
        Long daysPayable = findTheDaysPayable(records);
        earningsTemp.setBaseSalary(daysPayable * 800 + overtimePay);
        earningsTemp.setPayMonth(PayMonth);
        earningsTemp.setStartDate(StartDate);
        earningsTemp.setEndDate(EndDate);
        earningsTemp.setDaysPayable(daysPayable);
        earningsTemp.setCurrency("INR");
        earningsTemp.setHra(15000);
        earningsTemp.setFlexiPay(3000);
        earningsTemp.setBonus(4000);
        earningsTemp.setVariablePay(2000);
        earningsTemp.setShiftAllowance(earnings.getShiftAllowance());

        earningsTemp.calculateTotals();

        return earningsTemp;

    }

    public Long findTheDaysPayable(List<AttendanceRecord> records)
    {
        return records.stream()
                .filter((record) -> record.getAttendenceStatus().equals(AttendenceStatus.PRESENT)).count();

    }
}
