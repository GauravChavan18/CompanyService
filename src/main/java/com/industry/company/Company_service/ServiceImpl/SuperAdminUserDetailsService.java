package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.AuthEntity.SuperAdminEntity;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Repository.AdminRepository;
import com.industry.company.Company_service.Repository.CompanyRepository;
import com.industry.company.Company_service.Repository.SuperAdminRepository;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuperAdminUserDetailsService implements UserDetailsService {

    private final SuperAdminRepository superAdminRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Fetching Super Admin with email: {}", email);
        return superAdminRepository.findById(email)
                .orElseThrow(() -> {
                    log.warn("Super Admin not found for email: {}", email);
                    return new ResourceNotFoundException("Super Admin Not Found");
                });
    }

    public AdminEntity addAdminForCompany(AdminEntity adminEntity, String companyName) {
        log.info("Adding new Admin '{}' for company '{}'", adminEntity.getAdminEmail(), companyName);

        CompanyEntity company = companyRepository.findByCompanyName(companyName)
                .orElseThrow(() -> {
                    log.warn("Company not found with name: {}", companyName);
                    return new ResourceNotFoundException("Company Not Found With Name " + companyName);
                });

        adminEntity.setPassword(passwordEncoder.encode(adminEntity.getPassword()));
        adminEntity.setCompany(company);
        adminRepository.save(adminEntity);

        log.info("Admin '{}' successfully added for company '{}'", adminEntity.getAdminEmail(), companyName);
        return adminEntity;
    }

    public void updatePassword(String username, String newPassword) {
        log.info("Updating password for Super Admin: {}", username);

        SuperAdminEntity entity = superAdminRepository.findById(username)
                .orElseThrow(() -> {
                    log.warn("Super Admin not found for password update: {}", username);
                    return new ResourceNotFoundException("Not found");
                });

        entity.setPassword(passwordEncoder.encode(newPassword));
        superAdminRepository.save(entity);

        log.info("Password updated successfully for Super Admin: {}", username);
    }
}
