package com.industry.company.Company_service.Controller;



import com.industry.company.Company_service.AuthEntity.JWTUtil;
import com.industry.company.Company_service.AuthEntity.loginRequest;
import com.industry.company.Company_service.Repository.EmployeeAuthEntityRepo;
import com.industry.company.Company_service.ServiceImpl.CompositeUserDetailsService;
import com.industry.company.Company_service.ServiceImpl.EmployeeAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j

public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final CompositeUserDetailsService userDetailsService;

    private final JWTUtil jwtUtil;

    private final EmployeeAuthService employeeAuthService;

    private final EmployeeAuthEntityRepo employeeAuthEntityRepo;

    private final CompositeUserDetailsService compositeUserDetailsService;

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

//            System.out.println(userDetails);
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
        ResponseEntity<?> authEntity =compositeUserDetailsService.forgetPassword(loginrequest);
        return new ResponseEntity<>(authEntity, HttpStatus.CREATED);

    }

}



