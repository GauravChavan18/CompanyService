package com.industry.company.Company_service.AuthEntity;


import com.industry.company.Company_service.Repository.SuperAdminRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperAdminSeeder {

    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedSuperAdmin() {
        String defaultEmail = "superadmin@company.com";
        String defaultPassword = "Super@123";

        if (!superAdminRepository.existsById(defaultEmail)) {
            SuperAdminEntity admin = new SuperAdminEntity();
            admin.setEmail(defaultEmail);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setRole(Roles.SUPER_ADMIN);

            superAdminRepository.save(admin);

            System.out.println("✅ Super Admin created: " + defaultEmail + " / " + defaultPassword);
        } else {
            System.out.println("ℹ️ Super Admin already exists.");
        }
    }
}

