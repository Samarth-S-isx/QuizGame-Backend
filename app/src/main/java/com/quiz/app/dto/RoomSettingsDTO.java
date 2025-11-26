package com.quiz.app.dto;

public class RoomSettingsDTO {
    private String topic;
    private int numQuestions;
    private int timePerQuestion;
    private String difficulty;
    // Constructors
    public RoomSettingsDTO() {}

    public RoomSettingsDTO(String topic, int numQuestions, int timePerQuestion,String difficulty) {
        this.topic = topic;
        this.numQuestions = numQuestions;
        this.timePerQuestion = timePerQuestion;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(int timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
