package com.khc.enrollmentFront.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class SubjectListResponse {
    List<SubjectResponse> subjects;
}
