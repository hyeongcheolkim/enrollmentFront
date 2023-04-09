package com.khc.enrollmentFront.controller.dto;

import com.khc.enrollmentFront.type.ScoreType;
import lombok.Data;

@Data
public class GradeDTO {
    private Long enrollmentId;
    private Long prevPage;
    private ScoreType scoreType;
}
