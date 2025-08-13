package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto companyDto) {
        log.info("Received request to add new company: {}", companyDto.getCompanyName());

        try {
            CompanyDto newCompanyDto = companyService.addCompany(companyDto);
            log.info("Company '{}' successfully added with ID: {}", newCompanyDto.getCompanyName(), newCompanyDto.getCompanyName());
            return new ResponseEntity<>(newCompanyDto, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error("Error while adding company: {}", companyDto.getCompanyName(), e);
            return new ResponseEntity<>("Failed to add company", HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        log.info("Fetching all companies");

        try {
            List<CompanyDto> companyDtos = companyService.getAllCompanies();
            log.info("Fetched {} companies", companyDtos.size());
            return new ResponseEntity<>(companyDtos, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error("Error fetching all companies", e);
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyDto> getCompanyByName(@PathVariable String name) {
        log.info("Fetching company by name: {}", name);

        try {
            CompanyDto companyDto = companyService.getCompanyByName(name);
            if (companyDto != null) {
                log.info("Company found: {}", companyDto.getCompanyName());
            } else {
                log.warn("No company found with name: {}", name);
            }
            return new ResponseEntity<>(companyDto, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error("Error fetching company by name: {}", name, e);
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(500));
        }
    }
}
