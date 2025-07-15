package com.industry.company.Company_service.Repository;

import com.industry.company.Company_service.Entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendenceRepository extends JpaRepository<AttendanceRecord, Long> {

 //   List<AttendanceRecord> findByEmployeeIdAndTodayDateBetween( Long employeeId, LocalDate startDate, LocalDate endDate );

    @Query(
            value = """
            SELECT *
            FROM attendance_record
            WHERE employee_id = :employeeId
              AND today_date BETWEEN :startDate AND :endDate
        """,
            nativeQuery = true
    )
    List<AttendanceRecord> findByEmployeeIdAndTodayDateBetween(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
