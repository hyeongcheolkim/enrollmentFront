package com.khc.enrollmentFront.controller;

import com.khc.enrollmentFront.controller.dto.CourseDTO;
import com.khc.enrollmentFront.controller.dto.CourseOpenDTO;
import com.khc.enrollmentFront.controller.dto.CourseProhibitDTO;
import com.khc.enrollmentFront.controller.dto.PageableRequest;
import com.khc.enrollmentFront.controller.response.ClassroomListResponse;
import com.khc.enrollmentFront.controller.response.CoursePageableResponse;
import com.khc.enrollmentFront.controller.response.DepartmentListResponse;
import com.khc.enrollmentFront.controller.response.SubjectListResponse;
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
@RequestMapping("/course")
@Slf4j
public class CourseController {

    private final WebClient webClient;
    private final WebClientHelper webClientHelper;

    @GetMapping("/student")
    public Mono<Rendering> courseStudentListGET(@ModelAttribute Mono<PageableRequest> pageableRequestMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(pageableRequestMono, webSessionMono)
                .flatMap(tuple -> {
                    final PageableRequest pageableRequest = tuple.getT1();
                    final WebSession webSession = tuple.getT2();
                    return webClient.post()
                            .uri(uri -> uri.path("/course/list")
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
                            .bodyToMono(CoursePageableResponse.class)
                            .flatMap(coursePageableResponse -> Mono.just(Rendering.view("/student/course")
                                    .modelAttribute("CoursePageableResponse", coursePageableResponse)
                                    .build()));
                });
    }

    @GetMapping("/professor")
    public Mono<Rendering> courseProfessorListGET(@ModelAttribute Mono<PageableRequest> pageableRequestMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(pageableRequestMono, webSessionMono, webClientHelper.departmentListResponseMono())
                .flatMap(tuple -> {
                    final PageableRequest pageableRequest = tuple.getT1();
                    final WebSession webSession = tuple.getT2();
                    final DepartmentListResponse departmentListResponse = tuple.getT3();

                    return webClient.post()
                            .uri(uri -> uri.path("/course/professor/list")
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
                            .bodyToMono(CoursePageableResponse.class)
                            .flatMap(coursePageableResponse -> Mono.just(Rendering.view("/professor/course")
                                    .modelAttribute("CoursePageableResponse", coursePageableResponse)
                                    .modelAttribute("DepartmentListResponse", departmentListResponse)
                                    .build()));
                });
    }

    @PostMapping("/enroll")
    public Mono<Rendering> enrollCoursePOST
            (@ModelAttribute Mono<CourseDTO> courseDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(courseDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var courseDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/enroll/enroll")
                                    .queryParam("courseId", courseDTO.getCourseId())
                                    .build())
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo(
                                    String.format("/course/student?page=%d", courseDTO.getPrevPage())).build()));
                });
    }

    @GetMapping("/open")
    public Mono<Rendering> openCourseGET() {
        return Mono.zip(webClientHelper.subjectListResponseMono(),
                        webClientHelper.departmentListResponseMono(),
                        webClientHelper.classroomListResponseMono())
                .flatMap(tuple -> {
                    final SubjectListResponse subjectListResponse = tuple.getT1();
                    final DepartmentListResponse departmentListResponse = tuple.getT2();
                    final ClassroomListResponse classroomListResponse = tuple.getT3();

                    return Mono.just(Rendering.view("/professor/course-open")
                            .modelAttribute("subjectListResponse", subjectListResponse)
                            .modelAttribute("departmentListResponse", departmentListResponse)
                            .modelAttribute("classroomListResponse", classroomListResponse)
                            .build());
                });
    }


    @PostMapping("/open")
    public Mono<Rendering> openCoursePOST
            (@ModelAttribute Mono<CourseOpenDTO> courseOpenDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(courseOpenDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var courseOpenDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri("/course/open")
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(courseOpenDTO)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo("/course/open").build()));
                });
    }

    @PostMapping("/prohibit-dept")
    public Mono<Rendering> prohibitDeptPOST
            (@ModelAttribute Mono<CourseProhibitDTO> courseProhibitDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(courseProhibitDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var courseCloseDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/course/prohibit-dept")
                                    .queryParam("courseId", courseCloseDTO.getCourseId())
                                    .queryParam("departmentId", courseCloseDTO.getDepartmentId())
                                    .build())
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo(
                                    String.format("/course/professor?page=%d", courseCloseDTO.getPrevPage())).build()));
                });
    }

    @PostMapping("/close")
    public Mono<Rendering> closeCoursePOST
            (@ModelAttribute Mono<CourseDTO> courseCloseDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(courseCloseDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var courseCloseDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/course/close")
                                    .queryParam("courseId", courseCloseDTO.getCourseId())
                                    .build())
                            .cookie("JSESSIONID", webSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo(
                                    String.format("/course/professor?page=%d", courseCloseDTO.getPrevPage())).build()));
                });
    }

}
