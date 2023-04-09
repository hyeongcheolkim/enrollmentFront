package com.khc.enrollmentFront.controller;

import com.khc.enrollmentFront.controller.dto.BasketDTO;
import com.khc.enrollmentFront.controller.dto.CourseDTO;
import com.khc.enrollmentFront.controller.dto.PageableRequest;
import com.khc.enrollmentFront.controller.response.BasketPageableResponse;
import com.khc.enrollmentFront.exception.ApiException;
import com.khc.enrollmentFront.exception.dto.ApiExceptionDTO;
import com.khc.enrollmentFront.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/basket")
@Slf4j
public class BasketController {

    private final WebClient webClient;

    @PostMapping("/put")
    Mono<Rendering> putBasket(@ModelAttribute Mono<CourseDTO> courseDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(courseDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var courseDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/basket/put")
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

    @GetMapping("/list")
    public Mono<Rendering> basketListGET(@ModelAttribute Mono<PageableRequest> pageableRequestMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(pageableRequestMono, webSessionMono)
                .flatMap(webSession -> {
                    final PageableRequest pageableRequest = webSession.getT1();
                    final WebSession t2 = webSession.getT2();

                    return webClient.post()
                            .uri(uri -> uri.path("/student/basket")
                                    .queryParam("page", pageableRequest.getPage())
                                    .queryParam("size", pageableRequest.getSize())
                                    .queryParamIfPresent("sort", Optional.ofNullable(pageableRequest.getSort()))
                                    .build()
                            )
                            .cookie("JSESSIONID", t2.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(BasketPageableResponse.class)
                            .flatMap(resp -> Mono.just(Rendering.view("/student/basket")
                                    .modelAttribute("BasketPageableResponse", resp)
                                    .build()));
                });
    }

    @PostMapping("/erase")
    public Mono<Rendering> eraseBasketPOST(@ModelAttribute Mono<BasketDTO> basketDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(basketDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var basketDTO = tuple.getT1();
                    final var WebSession = tuple.getT2();
                    return webClient.post()
                            .uri(uri -> uri.path("/basket/erase")
                                    .queryParam("basketId", basketDTO.getBasketId())
                                    .build())
                            .cookie("JSESSIONID", WebSession.getAttribute(SessionConst.JSESSION_VALUE))
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .onStatus(HttpStatus::is4xxClientError, response ->
                                    response.bodyToMono(ApiExceptionDTO.class).flatMap(e -> Mono.error(new ApiException(e))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(Rendering.redirectTo(
                                    String.format("/basket/list?page=%d", basketDTO.getPrevPage())).build()));
                });
    }

    @PostMapping("/enroll")
    public Mono<Rendering> enrollBasketPOST(@ModelAttribute Mono<CourseDTO> courseDTOMono, Mono<WebSession> webSessionMono) {
        return Mono.zip(courseDTOMono, webSessionMono)
                .flatMap(tuple -> {
                    final var courseDTO = tuple.getT1();
                    final var webSession = tuple.getT2();

                    log.info("courseId : {}", courseDTO.getCourseId());

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
                                    String.format("/basket/list?page=%d", courseDTO.getPrevPage())).build()));
                });
    }

}
