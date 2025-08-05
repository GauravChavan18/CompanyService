package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.Repository.AdminRepository;
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
public class AdminUserDetails implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("fetching Admin");
        return adminRepository.findById(email).orElseThrow(()-> new ResourceNotFoundException("Admin not found"));
    }

    public void updatePassword(String username, String newPassword) {
        AdminEntity entity = adminRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        entity.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(entity);
    }
}
