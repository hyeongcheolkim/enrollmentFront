package com.khc.enrollmentFront.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageableRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String sort;
}
