package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Dto.PunchRequestDto;
import com.industry.company.Company_service.Entity.AttendanceRecord;

import java.util.List;


public interface AttendenceRecordService {

    public AttendanceRecord FillAttendenceForDay(PunchRequestDto punchRequestDto, Long employeeId );
}
