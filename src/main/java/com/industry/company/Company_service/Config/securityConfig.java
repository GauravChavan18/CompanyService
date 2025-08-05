package com.industry.company.Company_service.Config;


import com.industry.company.Company_service.AuthEntity.JWTUtil;
import com.industry.company.Company_service.AuthEntity.JWTValidationFilter;
import com.industry.company.Company_service.ServiceImpl.CompositeUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class securityConfig {

    private final CompositeUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http , AuthenticationManager authenticationManager , JWTUtil jwtUtil) throws Exception {

        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(jwtUtil,userDetailsService);
        log.info("in security filter chain");
        http
                .csrf().disable() // Disable CSRF for simplicity (but do this carefully in prod)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/forgotPassword","/login").permitAll()
                        .anyRequest().authenticated()                 // Secure all other endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
