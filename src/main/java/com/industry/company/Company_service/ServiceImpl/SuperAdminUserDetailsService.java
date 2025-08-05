package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.AuthEntity.SuperAdminEntity;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Repository.AdminRepository;
import com.industry.company.Company_service.Repository.CompanyRepository;
import com.industry.company.Company_service.Repository.SuperAdminRepository;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import com.openhtmltopdf.css.parser.property.PrimitivePropertyBuilders;
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
        log.info("fetching Super Admin");
        return (UserDetails) superAdminRepository.findById(email).orElseThrow(()-> new ResourceNotFoundException("Super Admin Not Found"));
    }

    public AdminEntity addAdminForCompany(AdminEntity adminEntity , String companyName) {

        CompanyEntity company =companyRepository
                .findByCompanyName(companyName)
                .orElseThrow(()-> new ResourceNotFoundException("Company Not Found With Name "+ companyName));

        adminEntity.setPassword(passwordEncoder.encode(adminEntity.getPassword()));
        adminEntity.setCompany(company);
        adminRepository.save(adminEntity);

        return adminEntity;
    }

    public void updatePassword(String username, String newPassword) {
        SuperAdminEntity entity = superAdminRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        entity.setPassword(passwordEncoder.encode(newPassword));
        superAdminRepository.save(entity);
    }
}
