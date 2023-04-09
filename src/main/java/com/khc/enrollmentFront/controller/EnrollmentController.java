package com.khc.enrollmentFront.controller;

import com.khc.enrollmentFront.controller.dto.EnrollmentDTO;
import com.khc.enrollmentFront.controller.dto.GradeDTO;
import com.khc.enrollmentFront.controller.dto.PageableRequest;
import com.khc.enrollmentFront.controller.response.NotSemesterEnrollmentPageableResponse;
import com.khc.enrollmentFront.controller.response.OnSemesterEnrollmentPageableResponse;
import com.khc.enrollmentFront.exception.ApiException;
import com.khc.enrollmentFront.exception.dto.ApiExceptionDTO;
import com.khc.enrollmentFront.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/enroll")
@Slf4j
public class EnrollmentController {

    private final WebClient webClient;

    @GetMapping("/on-semester/student")
    public Mono<Rendering> courseOnSemesterStudentListGET(@ModelAttribute Mono<PageableRequest> pageableRequestMono,
                                                          Mono<WebSession> webSessionMono) {
        return Mono.zip(pageableRequestMono, webSessionMono)
                .flatMap(tuple -> {
                    final PageableRequest pageableRequest = tuple.getT1();
                    final WebSession webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/enroll/on-semester/student")
                                    .queryParam("page", pageableRequest.getPage())
                                    .queryParam("size", pageableRequest.getSize())
                                    .queryParamIfPresent("sort", Optional.ofNullable(pageableRequest.getSort()))
                                    .build()
                            )
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(OnSemesterEnrollmentPageableResponse.class)
                            .flatMap(resp -> Mono.just(Rendering.view("/student/on-semester")
                                    .modelAttribute("OnSemesterEnrollmentPageableResponse", resp)
                                    .build()));
                });
    }

    @GetMapping("/not-semester/student")
    public Mono<Rendering> courseNotSemesterStudentListGET(
            @ModelAttribute Mono<PageableRequest> pageableRequestMono,
            Mono<WebSession> webSessionMono) {
        return Mono.zip(pageableRequestMono, webSessionMono)
                .flatMap(tuple -> {
                    final PageableRequest pageableRequest = tuple.getT1();
                    final WebSession webSession = tuple.getT2();


                    return webClient.post()
                            .uri(uri -> uri.path("/enroll/not-semester/student")
                                    .queryParam("page", pageableRequest.getPage())
                                    .queryParam("size", pageableRequest.getSize())
                                    .queryParamIfPresent("sort", Optional.ofNullable(pageableRequest.getSort()))
                                    .build()
                            )
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(NotSemesterEnrollmentPageableResponse.class)
                            .flatMap(notSemesterEnrollmentPageableResponse -> Mono.just(Rendering.view("/student/not-semester")
                                    .modelAttribute("NotSemesterEnrollmentPageableResponse", notSemesterEnrollmentPageableResponse)
                                    .build()));
                });
    }

    @GetMapping("/on-semester/professor")
    public Mono<Rendering> courseOnSemesterProfessorListGET(@ModelAttribute Mono<PageableRequest> pageableRequestMono,
                                                            Mono<WebSession> webSessionMono) {
        return Mono.zip(pageableRequestMono, webSessionMono)
                .flatMap(tuple -> {
                    final PageableRequest pageableRequest = tuple.getT1();
                    final WebSession webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/enroll/on-semester/professor")
                                    .queryParam("page", pageableRequest.getPage())
                                    .queryParam("size", pageableRequest.getSize())
                                    .queryParamIfPresent("sort", Optional.ofNullable(pageableRequest.getSort()))
                                    .build()
                            )
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(OnSemesterEnrollmentPageableResponse.class)
                            .flatMap(resp -> Mono.just(Rendering.view("/professor/on-semester")
                                    .modelAttribute("OnSemesterEnrollPageableResponse", resp)
                                    .build()));
                });
    }

    @PostMapping("/drop")
    Mono<Rendering> dropEnroll(@ModelAttribute Mono<EnrollmentDTO> enrollmentDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(enrollmentDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var enrollmentDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/enroll/drop")
                                    .queryParam("enrollmentId", enrollmentDTO.getEnrollmentId())
                                    .build())
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo(
                                    String.format("/enroll/on-semester/student?page=%d", enrollmentDTO.getPrevPage())).build()));
                });
    }

    @PostMapping("/grade")
    public Mono<Rendering> gradeCoursePOST(@ModelAttribute Mono<GradeDTO> gradeDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(gradeDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var gradeDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/enroll/grade")
                                    .queryParam("enrollmentId", gradeDTO.getEnrollmentId())
                                    .queryParam("scoreType", gradeDTO.getScoreType())
                                    .build())
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo(
                                    String.format("/enroll/on-semester/professor?page=%d", gradeDTO.getPrevPage())).build()));
                });
    }
}
