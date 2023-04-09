package com.khc.enrollmentFront.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final WebClient webClient;

    @GetMapping("/info")
    public Mono<Rendering> mainPage(){
        return Mono.just(Rendering.view("/student/info").build());
    }
}
