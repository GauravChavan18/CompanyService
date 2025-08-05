package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AuthStatus;
import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails loadUserByUsername(String email){

        try{
            return employeeAuthService.loadUserByUsername(email);
        }
        catch (ResourceNotFoundException e)
        {
            try {
                return superAdminUserDetailsService.loadUserByUsername(email);
            } catch (ResourceNotFoundException ex2) {
                try {
                    return adminUserDetails.loadUserByUsername(email);
                }
                catch (ResourceNotFoundException ex3)
                {
                    throw new ResourceNotFoundException("User not found");
                }
            }
        }

    }



    public ResponseEntity<?> forgetPassword(loginRequest loginrequest) {
            String username = loginrequest.getEmail();
            String password = loginrequest.getPassword();
            String newPassword = loginrequest.getNewPassword();



        UserDetails userDetails = null;
        String updatedBy = "";

        // Try each user type in order
        try {
            userDetails = superAdminUserDetailsService.loadUserByUsername(username);
            updatedBy = "SUPERADMIN";
        } catch (ResourceNotFoundException e1) {
            try {
                userDetails = adminUserDetails.loadUserByUsername(username);
                updatedBy = "ADMIN";
            } catch (ResourceNotFoundException e2) {
                try {
                    userDetails = employeeAuthService.loadUserByUsername(username);
                    updatedBy = "EMPLOYEE";
                } catch (ResourceNotFoundException e3) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
            }
        }


            Boolean matches = passwordEncoder.matches(password ,userDetails.getPassword());

            log.info("Matches :{}",matches);
            if(username.equals(userDetails.getUsername()) && matches)
            {
                switch (updatedBy) {
                    case "SUPERADMIN" -> superAdminUserDetailsService.updatePassword(username, newPassword);
                    case "ADMIN" -> adminUserDetails.updatePassword(username, newPassword);
                    case "EMPLOYEE" -> employeeAuthService.updatePassword(username, newPassword);
                }
            }
            else{
                return new ResponseEntity<>("Details Mismatched" , HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(userDetails , HttpStatus.CREATED);
        }


}
