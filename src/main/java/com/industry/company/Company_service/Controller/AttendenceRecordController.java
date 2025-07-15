package com.industry.company.Company_service.Controller;

import com.industry.company.Company_service.Entity.AttendanceRecord;
import com.industry.company.Company_service.Service.AttendenceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class AttendenceRecordController {

    private  final AttendenceRecordService attendenceRecordService;

    @PostMapping("/{id}/{PayMonth}")
    public ResponseEntity<AttendanceRecord> FillAttendenceForDay(@RequestBody AttendanceRecord attendanceRecord, @PathVariable Long id )
    {
        AttendanceRecord attendancerecord = attendenceRecordService.FillAttendenceForDay(attendanceRecord,id);

        return new ResponseEntity<>(attendancerecord, HttpStatus.CREATED);
    }
}
