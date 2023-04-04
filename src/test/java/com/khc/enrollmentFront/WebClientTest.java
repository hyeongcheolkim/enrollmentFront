package com.khc.enrollmentFront;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest
public class WebClientTest {

    @Autowired
    WebClient webClient;

    @Test
    void webClientTest() {
        webClient
                .get()
                .uri(uri -> uri.path("/api/student/login")
                        .queryParam("loginId", "s")
                        .queryParam("pw", "sa")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
    }
}
