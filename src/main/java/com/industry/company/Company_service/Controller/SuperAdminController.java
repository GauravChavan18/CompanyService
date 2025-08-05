package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.ServiceImpl.SuperAdminUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
public class SuperAdminController {

    private final SuperAdminUserDetailsService superAdminUserDetailsService;

    @PostMapping("/createadmin/{companyName}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody AdminEntity adminEntity , @PathVariable String companyName)
    {
            AdminEntity admin =  superAdminUserDetailsService.addAdminForCompany(adminEntity , companyName);
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

}
