package com.khc.enrollmentFront.controller.response;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String type;
    private String name;
    private String departmentName;
}
