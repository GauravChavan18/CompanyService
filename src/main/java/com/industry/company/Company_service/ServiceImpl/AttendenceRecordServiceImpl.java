package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Entity.AttendanceRecord;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Repository.AttendenceRepository;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.PaySlipRepository;
import com.industry.company.Company_service.Service.AttendenceRecordService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendenceRecordServiceImpl implements AttendenceRecordService {

    private final AttendenceRepository attendenceRepository;

    private final PaySlipRepository paySlipRepository;

    private final EmploeeRepository emploeeRepository;


    @Override
    public AttendanceRecord FillAttendenceForDay(AttendanceRecord attendanceRecord,Long employeeId) {

        EmployeeEntity employee = emploeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));


        AttendanceRecord attendancerecord = new AttendanceRecord();

        String payMonth = attendanceRecord.getTodayDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);



        attendancerecord.setEmployee(employee);
        attendancerecord.setPayMonth(payMonth);
        attendancerecord.setTodayDate(attendanceRecord.getTodayDate());
        attendancerecord.setStartTime(attendanceRecord.getStartTime());
        attendancerecord.setEndTime(attendanceRecord.getEndTime());
        attendancerecord.setOvertimeHours(attendanceRecord.getOvertimeHours());


        attendenceRepository.save(attendancerecord);
        return attendancerecord;
    }
}
