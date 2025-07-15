package com.industry.company.Company_service.Service;

import com.industry.company.Company_service.Entity.AttendanceRecord;

import java.util.List;


public interface AttendenceRecordService {

    public AttendanceRecord FillAttendenceForDay(AttendanceRecord attendanceRecord , Long employeeId );
}
