package com.khc.enrollmentFront.controller.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String loginId;
    private String pw;
    private String name;
    private String type;
}
