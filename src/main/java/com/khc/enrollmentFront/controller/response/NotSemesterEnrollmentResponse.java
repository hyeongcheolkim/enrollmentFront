package com.khc.enrollmentFront.controller.response;

import com.khc.enrollmentFront.type.ScoreType;
import lombok.Data;

@Data
public class NotSemesterEnrollmentResponse {
    private Long enrollmentId;
    private Long subjectId;

    private Integer subjectCode;
    private Integer subjectCredit;
    private Integer division;
    private Integer courseOpenYear;
    private Integer courseOpenSemester;

    private ScoreType scoreType;
    private String subjectName;
}
