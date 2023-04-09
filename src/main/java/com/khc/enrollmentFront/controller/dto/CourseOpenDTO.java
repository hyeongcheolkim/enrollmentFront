package com.khc.enrollmentFront.controller.dto;

import com.khc.enrollmentFront.controller.response.CourseTime;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class CourseOpenDTO {
    private Long subjectId;

    private Long departmentId;

    private Long classroomId;

    private Integer capacity;

    private Integer studentYear;

    private Integer openYear;

    private Integer openSemester;

    private Integer division;

    private CourseTime courseTime = new CourseTime();

    private List<Long> prohibitedDepartmentIds = new ArrayList<>();
}
