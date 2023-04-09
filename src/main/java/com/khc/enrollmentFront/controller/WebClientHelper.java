package com.khc.enrollmentFront.controller;


import com.khc.enrollmentFront.controller.response.ClassroomListResponse;
import com.khc.enrollmentFront.controller.response.DepartmentListResponse;
import com.khc.enrollmentFront.controller.response.SubjectListResponse;
import com.khc.enrollmentFront.exception.ApiException;
import com.khc.enrollmentFront.exception.dto.ApiExceptionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientHelper {

    private final WebClient webClient;

    public Mono<SubjectListResponse> subjectListResponseMono() {
        return webClient.post()
                .uri("/subject/list")
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                .bodyToMono(SubjectListResponse.class);
    }

    public Mono<ClassroomListResponse> classroomListResponseMono() {
        return webClient.post()
                .uri("/classroom/list")
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                .bodyToMono(ClassroomListResponse.class);
    }

    public Mono<DepartmentListResponse> departmentListResponseMono() {
        return webClient.post()
                .uri("/department/list")
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                .bodyToMono(DepartmentListResponse.class);
    }
}
