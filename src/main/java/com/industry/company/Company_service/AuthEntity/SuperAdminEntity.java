package com.industry.company.Company_service.AuthEntity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
public class SuperAdminEntity implements UserDetails {

    @Id
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + this.role.name());
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
