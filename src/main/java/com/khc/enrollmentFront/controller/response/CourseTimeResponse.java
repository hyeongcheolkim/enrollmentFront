package com.khc.enrollmentFront.controller.response;

import com.khc.enrollmentFront.type.Day;
import lombok.Data;

@Data
public class CourseTimeResponse {
    Day day;
    Integer startHour;
    Integer endHour;
}
