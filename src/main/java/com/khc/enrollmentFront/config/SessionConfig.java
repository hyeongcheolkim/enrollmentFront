package com.khc.enrollmentFront.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionIdResolver;

import java.time.Duration;

@Configuration
public class SessionConfig {
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("session");
        resolver.setCookieMaxAge(Duration.ofMinutes(30));
        resolver.addCookieInitializer(responseCookieBuilder -> responseCookieBuilder.path("/"));
        resolver.addCookieInitializer(responseCookieBuilder -> responseCookieBuilder.sameSite("Lax"));
        return resolver;
    }
}
