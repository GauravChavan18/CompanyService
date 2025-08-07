package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.loginRequest;
import com.industry.company.Company_service.Repository.AdminRepository;
import com.industry.company.Company_service.Repository.EmployeeAuthEntityRepo;
import com.industry.company.Company_service.Repository.SuperAdminRepository;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompositeUserDetailsService implements UserDetailsService {

    private final EmployeeAuthService employeeAuthService;
    private final SuperAdminUserDetailsService superAdminUserDetailsService;
    private final AdminUserDetails adminUserDetails;

    private final PasswordEncoder passwordEncoder;
    private final EmployeeAuthEntityRepo employeeAuthEntityRepo;
    private final SuperAdminRepository superAdminRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Attempting to load user by email: {}", email);

        try {
            log.debug("Trying EmployeeAuthService...");
            return employeeAuthService.loadUserByUsername(email);
        } catch (ResourceNotFoundException e) {
            log.debug("User not found in EmployeeAuthService.");
            try {
                log.debug("Trying SuperAdminUserDetailsService...");
                return superAdminUserDetailsService.loadUserByUsername(email);
            } catch (ResourceNotFoundException ex2) {
                log.debug("User not found in SuperAdminUserDetailsService.");
                try {
                    log.debug("Trying AdminUserDetails...");
                    return adminUserDetails.loadUserByUsername(email);
                } catch (ResourceNotFoundException ex3) {
                    log.warn("User not found in any user service for email: {}", email);
                    throw new ResourceNotFoundException("User not found");
                }
            }
        }
    }

    public ResponseEntity<?> forgetPassword(loginRequest loginrequest) {
        String username = loginrequest.getEmail();
        String password = loginrequest.getPassword();
        String newPassword = loginrequest.getNewPassword();

        log.info("Password reset requested for username: {}", username);

        UserDetails userDetails = null;
        String updatedBy = "";

        // Try each user type in order
        try {
            log.debug("Checking SuperAdmin...");
            userDetails = superAdminUserDetailsService.loadUserByUsername(username);
            updatedBy = "SUPERADMIN";
        } catch (ResourceNotFoundException e1) {
            log.debug("Not found in SuperAdmin.");
            try {
                log.debug("Checking Admin...");
                userDetails = adminUserDetails.loadUserByUsername(username);
                updatedBy = "ADMIN";
            } catch (ResourceNotFoundException e2) {
                log.debug("Not found in Admin.");
                try {
                    log.debug("Checking Employee...");
                    userDetails = employeeAuthService.loadUserByUsername(username);
                    updatedBy = "EMPLOYEE";
                } catch (ResourceNotFoundException e3) {
                    log.warn("User not found for password reset: {}", username);
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
            }
        }

        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        log.debug("Password match result for {}: {}", username, matches);

        if (username.equals(userDetails.getUsername()) && matches) {
            log.info("Password matched for user: {}", username);
            switch (updatedBy) {
                case "SUPERADMIN" -> superAdminUserDetailsService.updatePassword(username, newPassword);
                case "ADMIN" -> adminUserDetails.updatePassword(username, newPassword);
                case "EMPLOYEE" -> employeeAuthService.updatePassword(username, newPassword);
                default -> log.error("Unknown user type: {}", updatedBy);
            }
        } else {
            log.warn("Password or username mismatch for {}", username);
            return new ResponseEntity<>("Details Mismatched", HttpStatus.NOT_FOUND);
        }

        log.info("Password successfully updated for user: {}", username);
        return new ResponseEntity<>(userDetails, HttpStatus.CREATED);
    }
}
