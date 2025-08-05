package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AuthStatus;
import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
import com.industry.company.Company_service.AuthEntity.loginRequest;
import com.industry.company.Company_service.Repository.EmployeeAuthEntityRepo;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeAuthService implements UserDetailsService {

    public final EmployeeAuthEntityRepo employeeAuthEntityRepo;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("fetching employee");
        return employeeAuthEntityRepo.findById(email).orElseThrow(()-> new ResourceNotFoundException("user not found"));

    }

    public ResponseEntity<?> forgetPassword(loginRequest loginrequest) {
        String username = loginrequest.getEmail();
        String password = loginrequest.getPassword();
        String newPassword = loginrequest.getNewPassword();

        EmployeeAuthEntity employeeauth = employeeAuthEntityRepo.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("employe not found"));

//        log.info("employee auth {}",employeeauth);
        Boolean matches = passwordEncoder.matches(password ,employeeauth.getPassword());

        log.info("Matches :{}",matches);
        if(username.equals(employeeauth.getUsername()) && matches)
        {
            employeeauth.setPassword(passwordEncoder.encode(newPassword));
            employeeauth.setStatus(AuthStatus.PASSWORD_SET);
            employeeAuthEntityRepo.save(employeeauth);
        }
        else{
            return new ResponseEntity<>("Details Mismatched" ,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employeeauth , HttpStatus.CREATED);
    }

    public void updatePassword(String username, String newPassword) {
        EmployeeAuthEntity entity = employeeAuthEntityRepo.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        entity.setPassword(passwordEncoder.encode(newPassword));
        employeeAuthEntityRepo.save(entity);
    }
}
