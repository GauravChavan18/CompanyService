package com.industry.company.Company_service.Controller;


import com.industry.company.Company_service.Entity.Earnings;
import com.industry.company.Company_service.Entity.PaySlip;
import com.industry.company.Company_service.Service.PaySlipService;
import com.industry.company.Company_service.advice.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/payslip")
@RequiredArgsConstructor
@EnableMethodSecurity
public class PaySlipController {

    private final PaySlipService paySlipService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/{PayMonth}/pdf")
    public void getPaySlipPdfByEmployeeIdAndMonth(@PathVariable Long id , @PathVariable String PayMonth , HttpServletResponse response , Principal principal)
    {


        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=payslip_" + id + ".pdf");

            paySlipService.getPaySlipPdfByEmployeeIdAndMonth(
                    id, PayMonth, response.getOutputStream(), principal.getName()
            );

        } catch (AccessDeniedException e) {
            sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "You are not authorized");
        } catch (Exception e) {
            sendJsonError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate PDF");
        }
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) {
        try {
            response.reset(); // Clears headers & output stream
            response.setContentType("application/json");
            response.setStatus(status);
            response.getWriter().write("{\"message\": \"" + message + "\"}");
        } catch (IOException ignored) {}
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaySlip> CreatePaySlip(@PathVariable long id , @RequestBody Earnings earnings , Principal principal)
    {
        PaySlip paySlip =paySlipService.CreatePaySlip(id ,earnings , principal.getName());
        return new ResponseEntity<>(paySlip,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/{PayMonth}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaySlip> CreatePaySlipByMonth(@PathVariable long id , @PathVariable String PayMonth , @RequestBody Earnings earnings,Principal principal)
    {
        PaySlip paySlip=paySlipService.CreatePaySlipByMonth(id,PayMonth,earnings,principal.getName());
        return new ResponseEntity<>(paySlip, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ApiResponse getAllPlayslipsByEmployeeeId(@PathVariable long id , Model model , Principal principal)
    {
        List<PaySlip> paySlipList = paySlipService.getAllPlayslipsByEmployeeeId(id , principal.getName());
        model.addAttribute("payslipList", paySlipList);
        return new ApiResponse(true, "Payslips fetched", paySlipList);

    }
}
