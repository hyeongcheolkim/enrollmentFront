package com.khc.enrollmentFront.exception;

import com.khc.enrollmentFront.exception.dto.ApiExceptionDTO;
import lombok.Data;

@Data
public class ApiException extends RuntimeException{
    private String exceptionType;
    private String exceptionName;
    private String message;

    public ApiException(ApiExceptionDTO apiExceptionDTO){
        this.exceptionName = apiExceptionDTO.getExceptionName();
        this.exceptionType = apiExceptionDTO.getExceptionType();
        this.message = apiExceptionDTO.getMessage();
    }
}
