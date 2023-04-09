package com.khc.enrollmentFront.controller;

import com.khc.enrollmentFront.controller.dto.ClassroomDTO;
import com.khc.enrollmentFront.controller.dto.DepartmentDTO;
import com.khc.enrollmentFront.controller.dto.SubjectDTO;
import com.khc.enrollmentFront.exception.ApiException;
import com.khc.enrollmentFront.exception.dto.ApiExceptionDTO;
import com.khc.enrollmentFront.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final WebClient webClient;

    @GetMapping("/info")
    public Mono<Rendering> mainPage() {
        return Mono.just(Rendering.view("/admin/info").build());
    }

    @PostMapping("/department/create")
    public Mono<Rendering> createSubject(@ModelAttribute Mono<DepartmentDTO> departmentDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(departmentDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final DepartmentDTO departmentDTO = tuple.getT1();
                    final WebSession webSession = tuple.getT2();

                    return webClient.post()
                            .uri("/department/create")
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(departmentDTO)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo("/admin/info").build()));
                });
    }

    @PostMapping("/subject/make")
    public Mono<Rendering> makeSubject(@ModelAttribute Mono<SubjectDTO> subjectDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(subjectDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final SubjectDTO subjectDTO = tuple.getT1();
                    final WebSession webSession = tuple.getT2();

                    return webClient.post()
                            .uri("/subject/make")
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(subjectDTO)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo("/admin/info").build()));
                });

    }

    @PostMapping("/classroom/create")
    public Mono<Rendering> createClassroom(@ModelAttribute Mono<ClassroomDTO> classroomDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(classroomDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final ClassroomDTO classroomDTO = tuple.getT1();
                    final WebSession webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/classroom/create")
                                    .queryParam("name", classroomDTO.getName())
                                    .queryParam("code", classroomDTO.getCode())
                                    .build())
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo("/admin/info").build()));
                });

    }


}
