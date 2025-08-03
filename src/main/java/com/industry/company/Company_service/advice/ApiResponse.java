package com.industry.company.Company_service.advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private LocalDateTime timeStamp;
    private T data;
    private ApiError error;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(boolean b, String payslipsFetched, T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }


}
