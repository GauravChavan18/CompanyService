package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest , Long> {
  List<LeaveRequest> findAllByEmployeeEmployeeId(Long employeeId);
}
