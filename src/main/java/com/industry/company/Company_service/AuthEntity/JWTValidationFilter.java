package com.industry.company.Company_service.AuthEntity;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug("Starting JWT validation for request: [{} {}]",
                request.getMethod(), request.getRequestURI());

        String token = extractJwtTokenFromRequest(request);
        log.debug("Token extracted: {}", token != null ? "Present" : "Not present");

        if (token != null) {
            String username = jwtUtil.getUserNameFromToken(token);
            log.debug("Username extracted from token: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("No authentication found in SecurityContext. Loading user details...");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.debug("User details loaded for: {}", userDetails.getUsername());

                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    log.debug("JWT token validated successfully for user: {}", username);

                    Claims claims = jwtUtil.extractAllClaims(token);
                    Object rolesObject = claims.get("roles");

                    List<GrantedAuthority> authorities = new ArrayList<>();

                    if (rolesObject instanceof List<?>) {
                        for (Object role : (List<?>) rolesObject) {
                            if (role instanceof String roleStr) {
                                log.debug("Role from token: {}", roleStr);
                                authorities.add(new SimpleGrantedAuthority(roleStr));
                            }
                        }
                    }

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("SecurityContext updated for user: {}", username);
                } else {
                    log.warn("JWT token validation failed for user: {}", username);
                }
            }
        } else {
            log.debug("No JWT token found in Authorization header.");
        }

        filterChain.doFilter(request, response);
        log.debug("JWT filter processing completed for request: [{} {}]",
                request.getMethod(), request.getRequestURI());
    }

    private String extractJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization header: {}", bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
