package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.AuthEntity.JWTUtil;
import com.industry.company.Company_service.AuthEntity.loginRequest;
import com.industry.company.Company_service.ServiceImpl.CompositeUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CompositeUserDetailsService compositeUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody loginRequest loginrequest) {
        String username = loginrequest.getEmail();
        log.info("Login attempt for email: {}", username);

        try {
            log.debug("Authenticating user: {}", username);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginrequest.getPassword())
            );

            if (auth.isAuthenticated()) {
                log.info("Authentication successful for user: {}", username);

                List<String> roles = auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                log.debug("Roles assigned to user {}: {}", username, roles);
                String token = jwtUtil.generateToken(username, roles, 5L);

                log.info("JWT token generated for user: {}", username);
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                log.warn("Authentication failed for user: {}", username);
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Authentication error for user: {}", username, e);
            return new ResponseEntity<>("Authentication error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody loginRequest loginrequest) {
        String username = loginrequest.getEmail();
        log.info("Forgot password request for email: {}", username);

        try {
            ResponseEntity<?> response = compositeUserDetailsService.forgetPassword(loginrequest);
            log.info("Forgot password process completed for email: {}", username);
            return response; // direct return, no wrapping
        } catch (Exception e) {
            log.error("Error in forgot password process for email: {}", username, e);
            return new ResponseEntity<>("Error processing forgot password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
