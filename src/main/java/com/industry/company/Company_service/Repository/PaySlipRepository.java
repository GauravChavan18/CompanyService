package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.Entity.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip,Long> {
    List<PaySlip> findByEmployeeEmployeeId(Long employeeId);

    Optional<PaySlip> findByEmployeeEmployeeIdAndEarnings_PayMonth(Long employeeId, String payMonth);


}
