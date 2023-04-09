package com.khc.enrollmentFront.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class ClassroomListResponse {
    List<ClassroomResponse> classrooms;
}
