package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.Entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {

    Optional<CompanyEntity> findByCompanyName(String companyName);
}
