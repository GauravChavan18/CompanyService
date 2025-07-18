package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest , Long> {
}
