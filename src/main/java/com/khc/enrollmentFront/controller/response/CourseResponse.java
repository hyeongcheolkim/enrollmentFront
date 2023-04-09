package com.khc.enrollmentFront.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class CourseResponse {
    private Long courseId;
    private Long subjectId;
    private Long departmentId;
    private Long professorId;

    private String subjectName;
    private String departmentName;
    private String professorName;

    private Integer subjectCode;
    private Integer capacity;
    private Integer openYear;
    private Integer openSemester;
    private Integer division;
    private CourseTime courseTime;

    private List<DepartmentResponse> prohibitedDepartments;
}
