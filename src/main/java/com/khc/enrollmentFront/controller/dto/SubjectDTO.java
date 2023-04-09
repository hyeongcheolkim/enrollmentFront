package com.khc.enrollmentFront.controller.dto;

import com.khc.enrollmentFront.type.SubjectType;
import lombok.Data;

@Data
public class SubjectDTO {
    private String name;

    private Integer credit;

    private Integer code;

    private SubjectType type;
}
