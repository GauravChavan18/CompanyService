package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.AuthEntity.SuperAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdminEntity , String> {


}
