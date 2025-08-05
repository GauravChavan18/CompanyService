package com.industry.company.Company_service.Controller;


import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@EnableMethodSecurity
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto companyDto)
    {
        CompanyDto newCompanyDto = companyService.addCompany(companyDto);

        return new ResponseEntity<>(newCompanyDto , HttpStatusCode.valueOf(200));
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CompanyDto>> getAllCompanies()
    {
        List<CompanyDto> companyDtos = companyService.getAllCompanies();
        return new ResponseEntity<>(companyDtos ,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyDto> getCompanyByName(@PathVariable String name)
    {
        CompanyDto companyDto = companyService.getCompanyByName(name);
        return new ResponseEntity<>(companyDto ,HttpStatusCode.valueOf(200));
    }
}
