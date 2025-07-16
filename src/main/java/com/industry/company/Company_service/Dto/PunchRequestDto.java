package com.industry.company.Company_service.Dto;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PunchRequestDto {

    private Long employeeId;

    private String punchType;

    private LocalDateTime timeStamp;
}
