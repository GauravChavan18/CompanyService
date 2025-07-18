package com.industry.company.Company_service.Controller;


import com.industry.company.Company_service.Entity.Earnings;
import com.industry.company.Company_service.Entity.PaySlip;
import com.industry.company.Company_service.Service.PaySlipService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/payslip")
@RequiredArgsConstructor
public class PaySlipController {

    private final PaySlipService paySlipService;


    @GetMapping("/{id}/{PayMonth}/pdf")
    public void getPaySlipPdfByEmployeeIdAndMonth(@PathVariable Long id , @PathVariable String PayMonth , HttpServletResponse response)
    {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=payslip_" + id + ".pdf");

        try (OutputStream outputStream = response.getOutputStream()) {
            paySlipService.getPaySlipPdfByEmployeeIdAndMonth(id,PayMonth,outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error writing PDF to response", e);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<PaySlip> CreatePaySlip(@PathVariable long id , @RequestBody Earnings earnings)
    {
        PaySlip paySlip =paySlipService.CreatePaySlip(id ,earnings);
        return new ResponseEntity<>(paySlip,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/{PayMonth}")
    public ResponseEntity<PaySlip> CreatePaySlipByMonth(@PathVariable long id , @PathVariable String PayMonth , @RequestBody Earnings earnings)
    {
        PaySlip paySlip=paySlipService.CreatePaySlipByMonth(id,PayMonth,earnings);
        return new ResponseEntity<>(paySlip, HttpStatus.CREATED);
    }
}
