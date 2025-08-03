package com.industry.company.Company_service.Controller;


import com.industry.company.Company_service.AuthEntity.AuthStatus;
import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
import com.industry.company.Company_service.AuthEntity.JWTUtil;
import com.industry.company.Company_service.AuthEntity.loginRequest;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Repository.EmployeeAuthEntityRepo;
import com.industry.company.Company_service.ServiceImpl.EmployeeAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JWTUtil jwtUtil;

    private final EmployeeAuthService employeeAuthService;

    private final EmployeeAuthEntityRepo employeeAuthEntityRepo;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody loginRequest loginrequest) {
        String username = loginrequest.getEmail();
        String password = loginrequest.getPassword();

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,password
                )
        );

        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String token = jwtUtil.generateToken(userDetails.getUsername(), roles, 5L);

            return new ResponseEntity<>(token, HttpStatus.OK); // ✅ just the token
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody loginRequest loginrequest)
    {
        ResponseEntity<?> employeeAuthEntity1 =employeeAuthService.forgetPassword(loginrequest);
        return new ResponseEntity<>(employeeAuthEntity1 , HttpStatus.CREATED);

    }

}



