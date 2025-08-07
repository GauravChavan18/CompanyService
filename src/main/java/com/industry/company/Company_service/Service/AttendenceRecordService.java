package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Dto.PunchRequestDto;
import com.industry.company.Company_service.Entity.AttendanceRecord;

import java.security.Principal;
import java.util.List;


public interface AttendenceRecordService {

    public AttendanceRecord FillAttendenceForDay(PunchRequestDto punchRequestDto, Long employeeId );

    public List<AttendanceRecord> FillAttendenceForMonth(List<PunchRequestDto> punchRequestDtos , Long employeeId , String adminEmail);

    public List<AttendanceRecord> GetAttendenceByEmployeeForMonth(Long employeeId , String payMonth , String email);
}
