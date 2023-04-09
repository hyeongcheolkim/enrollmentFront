package com.khc.enrollmentFront.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public Mono<Rendering> apiException(ApiException apiException) {
        return Mono.just(Rendering.view("error/api")
                .modelAttribute("exceptionName", apiException.getExceptionName())
                .modelAttribute("exceptionType", apiException.getExceptionType())
                .modelAttribute("message", apiException.getMessage())
                .build());
    }

    @ExceptionHandler
    public Mono<Rendering> exception(Exception exception) {
        return Mono.just(Rendering.view("error/api")
                .modelAttribute("exceptionName", exception.getClass().getSimpleName())
                .modelAttribute("message", exception.getMessage())
                .build());
    }
}
