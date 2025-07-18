package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Dto.PunchRequestDto;
import com.industry.company.Company_service.Entity.AttendanceRecord;
import com.industry.company.Company_service.Entity.AttendenceStatus;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Repository.AttendenceRepository;
import com.industry.company.Company_service.Repository.EmploeeRepository;
import com.industry.company.Company_service.Repository.PaySlipRepository;
import com.industry.company.Company_service.Service.AttendenceRecordService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendenceRecordServiceImpl implements AttendenceRecordService {

    private final AttendenceRepository attendenceRepository;

    private final PaySlipRepository paySlipRepository;

    private final EmploeeRepository emploeeRepository;


    @Override
    public AttendanceRecord FillAttendenceForDay(PunchRequestDto punchRequestDto, Long employeeId) {

        EmployeeEntity employee = emploeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));

        LocalDate todayDate =punchRequestDto.getTimeStamp().toLocalDate();

        AttendanceRecord attendancerecord = attendenceRepository.findByEmployeeEmployeeIdAndTodayDate(employeeId,todayDate).orElse(new AttendanceRecord());
        attendancerecord.setEmployee(employee);


        attendancerecord.setTodayDate(punchRequestDto.getTimeStamp().toLocalDate());
        String payMonth = attendancerecord.getTodayDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        attendancerecord.setPayMonth(payMonth);

        if(punchRequestDto.getPunchType().equalsIgnoreCase("In"))
        {
            attendancerecord.setStartTime(punchRequestDto.getTimeStamp().toLocalTime());
        }
        else if(punchRequestDto.getPunchType().equalsIgnoreCase("Out"))
        {
            attendancerecord.setEndTime(punchRequestDto.getTimeStamp().toLocalTime());
        }


        if (attendancerecord.getStartTime() != null && attendancerecord.getEndTime() != null) {
            Duration workDuration = Duration.between(attendancerecord.getStartTime(), attendancerecord.getEndTime());
            double workedHours = workDuration.toMinutes() / 60.0;
            attendancerecord.setAttendenceStatus(AttendenceStatus.PRESENT);
            if (workedHours > 9) {
                double overtime = workedHours - 9;
                attendancerecord.setOvertimeHours(overtime);
            } else {
                attendancerecord.setOvertimeHours(0.0);
            }
        }
        else if(attendancerecord.getStartTime() == null || attendancerecord.getEndTime() ==null)
        {
            attendancerecord.setAttendenceStatus(AttendenceStatus.ABSENT);
        }

        attendenceRepository.save(attendancerecord);
        return attendancerecord;
    }
}
