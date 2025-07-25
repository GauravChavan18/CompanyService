package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Dto.PunchRequestDto;
import com.industry.company.Company_service.Entity.AttendanceRecord;
import com.industry.company.Company_service.Service.AttendenceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class AttendenceRecordController {

    private  final AttendenceRecordService attendenceRecordService;

    @PostMapping("/{id}/{PayMonth}")
    public ResponseEntity<AttendanceRecord> FillAttendenceForDay(@RequestBody PunchRequestDto punchRequestDto, @PathVariable Long id )
    {
        AttendanceRecord attendancerecord = attendenceRecordService.FillAttendenceForDay(punchRequestDto,id);

        return new ResponseEntity<>(attendancerecord, HttpStatus.CREATED);
    }


    @PostMapping("/{id}/month/{PayMonth}")
    public ResponseEntity<List<AttendanceRecord>> FillAttendenceForMonth(@RequestBody List<PunchRequestDto> punchRequestDto, @PathVariable Long id )
    {
        List<AttendanceRecord> attendancerecord = attendenceRecordService.FillAttendenceForMonth(punchRequestDto,id);

        return new ResponseEntity<>(attendancerecord, HttpStatus.CREATED);
    }


    @GetMapping("/{id}/{payMonth}")
    public ResponseEntity<List<AttendanceRecord>> GetAttendenceByEmployeeForMonth(@PathVariable Long id , @PathVariable String payMonth)
    {
        List<AttendanceRecord> attendanceRecordList = attendenceRecordService.GetAttendenceByEmployeeForMonth(id,payMonth);

        return new ResponseEntity<>(attendanceRecordList , HttpStatus.FOUND);
    }
}
