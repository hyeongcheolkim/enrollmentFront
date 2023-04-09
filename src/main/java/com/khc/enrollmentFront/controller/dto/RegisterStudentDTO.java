package com.khc.enrollmentFront.controller.dto;

import lombok.Data;

@Data
public class RegisterStudentDTO {
    private String loginId;
    private String pw;
    private String name;
    private Long departmentId;
}
