package com.industry.company.Company_service.Repository;


import com.industry.company.Company_service.Entity.LeaveBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalanceEntity , Long> {
    LeaveBalanceEntity  findByEmployeeEmployeeId(Long employeeId);
}
