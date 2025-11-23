package com.quiz.app.service;

import com.quiz.app.config.OpenAIConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;

@Service
public class OpenAIService {

    private final WebClient webClient;
    private final OpenAIConfig config;

    public OpenAIService(WebClient.Builder webClientBuilder, OpenAIConfig config) {
        this.config = config;
        this.webClient = webClientBuilder.baseUrl(config.getApiUrl()).build();
    }

    public Mono<String> generateQuestions(String topic, int count) {
        String prompt = "Generate " + count + " multiple-choice questions on " + topic +
                ". Format as a JSON array with the following structure for each question: " +
                "{questionText: string, options: array of 4 strings, correctAnswer: integer index of correct option}";

        return webClient.post()
                .uri("") // already included in baseUrl
                .header("Authorization", "Bearer " + config.getApiKey())
                .bodyValue(Map.of(
                        "model", "gpt-4o",
                        "messages", List.of(Map.of("role", "user", "content", prompt))
                ))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(
                    Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> 
                            throwable instanceof WebClientResponseException.TooManyRequests
                        )
                );
    }
}
