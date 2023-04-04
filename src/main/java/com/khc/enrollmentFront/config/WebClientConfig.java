package com.khc.enrollmentFront.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:9090")
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }
}
