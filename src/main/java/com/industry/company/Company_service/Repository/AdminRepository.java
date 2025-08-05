package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity , String> {
}
