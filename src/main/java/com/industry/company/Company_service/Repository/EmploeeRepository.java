package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.Entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploeeRepository extends JpaRepository<EmployeeEntity,Long> {
    List<EmployeeEntity> findByCompanyCompanyName(String companyName);


    List<EmployeeEntity> findByAdminEntityAdminEmail(String admin);
}
