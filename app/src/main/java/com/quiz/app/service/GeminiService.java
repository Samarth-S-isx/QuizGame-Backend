    package com.quiz.app.service;

    import com.quiz.app.config.GeminiConfig;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.stereotype.Service;
    import org.springframework.web.reactive.function.client.WebClient;
    import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final GeminiConfig config;

    public GeminiService(WebClient.Builder webClientBuilder, GeminiConfig config) {
        this.config = config;
        // Set the baseUrl as configured without appending the API key
        this.webClient = webClientBuilder
                .baseUrl(config.getApiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> generateQuestions(String topic, int count) {
        String prompt = "Generate " + count + " multiple-choice questions on " + topic + ". Return the response as a properly formatted JSON array with the following structure for each question:\n" +
                    "{\n" +
                    "\"questionText\": \"string\",\n" +
                    "\"options\": [\"string1\", \"string2\", \"string3\", \"string4\"],\n" +
                    "\"correctAnswer\": integer index of the correct option (0-based)\n" +
                    "}\n" +
                    "Ensure that the JSON is properly formatted, with no extra text or formatting.";

        // Adjusted request body structure to match the expected input for Gemini
        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(Map.of("text", prompt)))
            )
        );

        // Add the API key as a query parameter
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", config.getApiKey())
                        .build())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("API Response: " + response)); // Log response before parsing
    }
}
