package com.khc.enrollmentFront.controller.response;

import com.khc.enrollmentFront.type.SubjectType;
import lombok.Data;

@Data
public class SubjectResponse {
    private Long subjectId;

    private String subjectName;

    private Integer credit;

    private Integer code;

    private SubjectType type;
}
