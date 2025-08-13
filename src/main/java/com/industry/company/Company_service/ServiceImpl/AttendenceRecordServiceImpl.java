package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.AuthEntity.AdminEntity;
import com.industry.company.Company_service.Dto.PunchRequestDto;
import com.industry.company.Company_service.Entity.AttendanceRecord;
import com.industry.company.Company_service.Entity.AttendenceStatus;
import com.industry.company.Company_service.Entity.EmployeeEntity;
import com.industry.company.Company_service.Entity.LeaveRequest;
import com.industry.company.Company_service.Repository.*;
import com.industry.company.Company_service.Service.AttendenceRecordService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendenceRecordServiceImpl implements AttendenceRecordService {

    private final AttendenceRepository attendenceRepository;

    private final PaySlipRepository paySlipRepository;

    private final EmploeeRepository emploeeRepository;

    private  final LeaveRepository leaveRepository;

    private final AdminRepository adminRepository;

    @Override
    public AttendanceRecord FillAttendenceForDay(PunchRequestDto punchRequestDto, Long employeeId) {

        EmployeeEntity employee = emploeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));

        List<LeaveRequest> leaveRequests = leaveRepository.findAllByEmployeeEmployeeId(employeeId);

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

        if(attendancerecord.getStartTime() == null && attendancerecord.getEndTime() == null)
        {
           Boolean hasLeave = leaveRequests.stream().anyMatch(
                   leaveRequest ->
                       !attendancerecord.getTodayDate().isBefore(leaveRequest.getLeaveStartDate()) && !attendancerecord.getTodayDate().isAfter(leaveRequest.getLeaveEndDate())
                   );

            if (hasLeave) {
                attendancerecord.setAttendenceStatus(AttendenceStatus.LEAVE);
            } else {
                attendancerecord.setAttendenceStatus(AttendenceStatus.ABSENT);
            }

        }
        else if (attendancerecord.getStartTime() != null && attendancerecord.getEndTime() != null) {
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
        else {
            attendancerecord.setAttendenceStatus(AttendenceStatus.ABSENT);
        }

        attendenceRepository.save(attendancerecord);
        return attendancerecord;
    }

    @Override
    public List<AttendanceRecord> FillAttendenceForMonth(List<PunchRequestDto> punchRequestDtos, Long employeeId, String adminEmail) {

        EmployeeEntity employee = emploeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));


        if(!employee.getAdminEntity().getAdminEmail().equals(adminEmail))
        {
            throw new AccessDeniedException("You are not authorized");
        }

       return punchRequestDtos.stream().map((punchRequestDto -> FillAttendenceForDay(punchRequestDto, employeeId))).toList();

    }

    @Override
    public List<AttendanceRecord> GetAttendenceByEmployeeForMonth(Long employeeId , String payMonth , String email) {


        boolean exists = emploeeRepository.findById(employeeId).isPresent()
                || adminRepository.findById(email).isPresent();

        if(!exists)
        {
            throw new ResourceNotFoundException("Entity Not Found");
        }

        List<AttendanceRecord> attendanceRecordList = attendenceRepository.findByEmployeeEmployeeIdAndPayMonth(employeeId ,payMonth);


        return attendanceRecordList;
    }
}
