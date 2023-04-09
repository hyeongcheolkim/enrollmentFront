package com.khc.enrollmentFront.controller.response;

import com.khc.enrollmentFront.type.Day;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseTime {
    private Day day;
    private Integer startHour;
    private Integer endHour;
}
