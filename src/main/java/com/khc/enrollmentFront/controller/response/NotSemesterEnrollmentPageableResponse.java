package com.khc.enrollmentFront.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class NotSemesterEnrollmentPageableResponse {
    List<NotSemesterEnrollmentResponse> content;

    private Long totalPages;
    private Long totalElements;
    private Long size;

    private Long number;
    private Long numberOfElements;
    private Long pageSize;
    private Long pageNumber;

    private Boolean first;
    private Boolean last;
    private Boolean empty;
}
