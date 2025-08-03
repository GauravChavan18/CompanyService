package com.industry.company.Company_service.AuthEntity;


import com.industry.company.Company_service.Entity.EmployeeEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
public class EmployeeAuthEntity implements UserDetails {

    @Id
    private String email; // Primary Key (or use @GeneratedValue + make email unique)

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private AuthStatus status;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
    private EmployeeEntity employee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + this.role.name());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
