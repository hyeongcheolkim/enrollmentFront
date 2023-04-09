package com.khc.enrollmentFront.controller;

import com.khc.enrollmentFront.controller.dto.LoginDTO;
import com.khc.enrollmentFront.controller.dto.RegisterDTO;
import com.khc.enrollmentFront.controller.response.DepartmentListResponse;
import com.khc.enrollmentFront.controller.response.LoginResponse;
import com.khc.enrollmentFront.exception.ApiException;
import com.khc.enrollmentFront.exception.dto.ApiExceptionDTO;
import com.khc.enrollmentFront.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final WebClient webClient;

    @GetMapping("/login")
    public Mono<Rendering> loginGET() {
        return Mono.just(Rendering.view("/login").build());
    }

    @PostMapping("/login")
    public Mono<Rendering> loginPOST(@ModelAttribute Mono<LoginDTO> loginDTOMono,
                                     Mono<WebSession> webSessionMono) {
        return Mono.zip(loginDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var loginDTO = tuple.getT1();
                    final var webSession = tuple.getT2();
                    final String finalPath = extractPath(loginDTO.getType());

                    return webClient.post()
                            .uri(uri -> uri.path(String.format("/%s/login", finalPath))
                                    .queryParam("loginId", loginDTO.getLoginId())
                                    .queryParam("pw", loginDTO.getPw())
                                    .build())
                            .exchangeToMono(clientResponse -> {
                                if (clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                                    return clientResponse.bodyToMono(ApiExceptionDTO.class)
                                            .flatMap(e -> Mono.error(new ApiException(e)));
                                final ResponseCookie jsessionid = clientResponse.cookies().getFirst("JSESSIONID");
                                webSession.getAttributes().putIfAbsent(SessionConst.JSESSION_VALUE, jsessionid.getValue());
                                return clientResponse.bodyToMono(LoginResponse.class);
                            })
                            .doOnNext(e -> log.info("LOGIN [ID: {}, type: {}, name: {}]", e.getId(), e.getType(), e.getName()))
                            .flatMap(e -> {
                                webSession.getAttributes().putIfAbsent(loginDTO.getType(), e);
                                if (e.getType().equals("LOGIN_STUDENT"))
                                    return Mono.just(Rendering.redirectTo("/student/info").build());
                                if (e.getType().equals("LOGIN_PROFESSOR"))
                                    return Mono.just(Rendering.redirectTo("/professor/info").build());
                                if (e.getType().equals("LOGIN_ADMIN"))
                                    return Mono.just(Rendering.redirectTo("/admin/info").build());
                                return Mono.empty();
                            });
                });
    }

    @GetMapping("/logout")
    public Mono<Rendering> logoutPOST(Mono<WebSession> webSessionMono) {
        return webSessionMono.flatMap(webSession -> {
            webSession.invalidate();
            return Mono.just(Rendering.redirectTo("/login").build());
        });
    }

    @GetMapping("/register")
    public Mono<Rendering> registerGET() {
        return Mono.just(Rendering.view("/register").build());
    }

    @PostMapping("/register")
    public Mono<Rendering> registerPOST(@ModelAttribute Mono<RegisterDTO> registerDTOMono) {
        return registerDTOMono.flatMap(registerDTO -> {
            final String finalPath = extractPath(registerDTO.getType());
            return webClient.post()
                    .uri(String.format("/%s/register", finalPath))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(registerDTO)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response ->
                            response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                    .bodyToMono(Void.class)
                    .then(Mono.just(Rendering.redirectTo("/login").build()));
        });
    }

    @GetMapping("/register/student")
    public Mono<Rendering> registerStudentGET() {
        return webClient.post()
                .uri("/department/list")
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                .bodyToMono(DepartmentListResponse.class)
                .flatMap(e -> Mono.just(Rendering.view("register-student")
                        .modelAttribute("DepartmentListResponse", e)
                        .build()));
    }

    @PostMapping("/register/student")
    public Mono<Rendering> registerStudentPOST(@ModelAttribute Mono<RegisterDTO> registerDTOMono) {
        return registerDTOMono.flatMap(registerDTO -> {
            final String finalPath = extractPath(registerDTO.getType());
            return webClient.post()
                    .uri(String.format("/student/register", finalPath))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(registerDTO)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response ->
                            response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                    .bodyToMono(Void.class)
                    .then(Mono.just(Rendering.redirectTo("/login").build()));
        });
    }

    private String extractPath(String type) {
        String ret = "any";
        if (type.equals(SessionConst.LOGIN_STUDENT))
            ret = "student";
        if (type.equals(SessionConst.LOGIN_ADMIN))
            ret = "admin";
        if (type.equals(SessionConst.LOGIN_PROFESSOR))
            ret = "professor";
        return ret;
    }
}
