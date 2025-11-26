package com.quiz.app.controller;

import com.quiz.app.model.Question;
import com.quiz.app.service.GeminiService;
import com.quiz.app.service.OpenAIService;
import com.quiz.app.service.QuestionService;

import java.util.List;

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
    @Autowired 
    private QuestionService questionService;

    // @GetMapping("/generate")
    // public Mono<String> generateQuestions(@RequestParam String topic, @RequestParam(defaultValue = "5") int count) {
    //     // return openAIService.generateQuestions(topic, count);
    //     return geminiService.generateQuestions(topic, count);
    // }
    @GetMapping("/generate")
    public List<Question> generateQuestions(@RequestParam String topic, @RequestParam(defaultValue = "5") int count,@RequestParam String difficulty) {
        System.err.println("Generating questions for topic: " + topic + " with count: " + count + " and difficulty: " + difficulty);
        // return openAIService.generateQuestions(topic, count);
        return questionService.generateQuestions(topic, count,difficulty);
    }
}
