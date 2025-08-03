package com.industry.company.Company_service.Repository;


import com.industry.company.Company_service.AuthEntity.EmployeeAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAuthEntityRepo extends JpaRepository<EmployeeAuthEntity , String> {
}
