package com.khc.enrollmentFront.exception;

public class NotAuthenticatedException extends RuntimeException{
    public NotAuthenticatedException(){
        super("로그인 하지 않았습니다");
    }
}
