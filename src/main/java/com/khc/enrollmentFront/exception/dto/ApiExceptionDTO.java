package com.khc.enrollmentFront.exception.dto;

import lombok.Data;

@Data
public class ApiExceptionDTO {
    private String exceptionType;
    private String exceptionName;
    private String message;
}
