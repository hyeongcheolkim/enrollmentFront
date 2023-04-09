package com.khc.enrollmentFront.controller.response;

import lombok.Data;

@Data
public class OnSemesterEnrollmentResponse {
    private Long enrollmentId;

    private Long subjectId;

    private String subjectName;

    private Integer subjectCode;

    private Integer division;

    private String studentName;

    private String studentDepartmentName;
}
