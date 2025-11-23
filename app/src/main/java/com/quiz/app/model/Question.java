package com.quiz.app.model;

import java.util.List;
import lombok.Data;
@Data
public class Question {

    private String questionText;
    private List<String> options;
    private int correctAnswer;
    
    public Question(String questionText, List<String> options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}
