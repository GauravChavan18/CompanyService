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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        String token = extractJwtTokenFromrequest(request);
        log.info("token : {}",token);

        if (token != null) {
            String username = jwtUtil.getUserNameFromToken(token);
            log.info(username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info(userDetails.getUsername());
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    Claims claims = jwtUtil.extractAllClaims(token);
                    Object rolesObject = claims.get("roles");

                    List<GrantedAuthority> authorities = new ArrayList<>();

                    if (rolesObject instanceof List<?>) {
                        for (Object role : (List<?>) rolesObject) {
                            if (role instanceof String roleStr) {
                                // Prefix "ROLE_" only if needed
                                log.info(roleStr);
                                authorities.add(new SimpleGrantedAuthority(roleStr));
                            }
                        }
                    }

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    private String extractJwtTokenFromrequest(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);

        }
        return null;
    }
}
