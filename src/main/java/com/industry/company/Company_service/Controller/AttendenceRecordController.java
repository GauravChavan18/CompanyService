package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Dto.PunchRequestDto;
import com.industry.company.Company_service.Entity.AttendanceRecord;
import com.industry.company.Company_service.Service.AttendenceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
@EnableMethodSecurity
public class AttendenceRecordController {

    private  final AttendenceRecordService attendenceRecordService;

    @PostMapping("/{id}/{PayMonth}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceRecord> FillAttendenceForDay(@RequestBody PunchRequestDto punchRequestDto, @PathVariable Long id )
    {
        AttendanceRecord attendancerecord = attendenceRecordService.FillAttendenceForDay(punchRequestDto,id);

        return new ResponseEntity<>(attendancerecord, HttpStatus.CREATED);
    }


    @PostMapping("/{id}/month/{PayMonth}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceRecord>> FillAttendenceForMonth(@RequestBody List<PunchRequestDto> punchRequestDto, @PathVariable Long id ,Principal principal)
    {
        List<AttendanceRecord> attendancerecord = attendenceRecordService.FillAttendenceForMonth(punchRequestDto,id , principal.getName());

        return new ResponseEntity<>(attendancerecord, HttpStatus.CREATED);
    }


    @GetMapping("/{id}/{payMonth}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AttendanceRecord>> GetAttendenceByEmployeeForMonth(@PathVariable Long id , @PathVariable String payMonth ,Principal principal)
    {
        List<AttendanceRecord> attendanceRecordList = attendenceRecordService.GetAttendenceByEmployeeForMonth(id,payMonth,principal.getName());

        return new ResponseEntity<>(attendanceRecordList , HttpStatus.FOUND);
    }
}
