package com.khc.enrollmentFront.controller.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String loginId;
    private String pw;
    private String type;
    private String departmentName;
}
