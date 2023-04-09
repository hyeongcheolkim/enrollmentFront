package com.khc.enrollmentFront.controller.response;

import lombok.Data;

@Data
public class BasketResponse {

    private Long basketId;
    private Long studentId;
    private Long courseId;
    private Long subjectId;

    private String studentName;
    private String subjectName;

    private Integer capacity;
    private Integer division;
    private Integer subjectCode;
}
