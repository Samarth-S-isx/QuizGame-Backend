package com.quiz.app.dto;

import com.quiz.app.model.UserResponse;

import java.util.List;

public class RoomHistoryDTO {
    private String roomCode;
    private String topic;
    private Integer timer;
    private Integer numberOfQuestions;
    private List<UserResponse> userResponses;

    public RoomHistoryDTO(String roomCode, String topic, Integer timer, Integer numberOfQuestions, List<UserResponse> userResponses) {
        this.roomCode = roomCode;
        this.topic = topic;
        this.timer = timer;
        this.numberOfQuestions = numberOfQuestions;
        this.userResponses = userResponses;
    }

    public List<UserResponse> getUserResponses() {
        return userResponses;
    }

    public void setUserResponses(List<UserResponse> userResponses) {
        this.userResponses = userResponses;
    }

    // Getters and Setters
    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
