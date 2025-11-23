package com.quiz.app.controller;

import com.quiz.app.service.GeminiService;
import com.quiz.app.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5173"})
public class QuestionController {

    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private GeminiService geminiService;

    @GetMapping("/generate")
    public Mono<String> generateQuestions(@RequestParam String topic, @RequestParam(defaultValue = "5") int count) {
        // return openAIService.generateQuestions(topic, count);
        return geminiService.generateQuestions(topic, count);
    }
}
