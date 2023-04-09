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

    static class A{
        int x = 1;
        int y = 2;
    }

    @Test
    void webClientTest() {
        final A a = new A();
        final Mono<A> stream = Mono.just(a);
        stream.subscribe(e -> System.out.println(e.x));
        stream.subscribe(e -> System.out.println(e.x));
        stream.subscribe(e -> System.out.println(e.x));
        stream.subscribe(e -> System.out.println(e.x));
        stream.subscribe(e -> System.out.println(e.x));
        stream.subscribe(e -> System.out.println(e.x));
        stream.subscribe(e -> System.out.println(e.x));
    }
}
