package com.industry.company.Company_service.AuthEntity;


import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
public class AdminEntity implements UserDetails {

    @Id
    private String email;

    private String Password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @ManyToOne
    @JoinColumn(name = "companyName" , nullable=false)
    public CompanyEntity company;

    @OneToMany(mappedBy = "adminEntity" , cascade = CascadeType.ALL)
    private List<EmployeeEntity> employeeEntities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + this.role.name());
    }

    @Override
    public String toString() {
        return "AdminEntity{" +
                "email='" + email + '\'' +
                ", Password='" + Password + '\'' +
                ", role=" + role +
                ", company=" + company +
                '}';
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
