package com.khc.enrollmentFront.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class StudentWebClient {

    private final WebClient webClient;

    private Mono<StudentResponse> loginStudent(String loginId, String pw){


    }
}
