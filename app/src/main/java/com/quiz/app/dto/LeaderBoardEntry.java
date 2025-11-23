// File: LeaderboardEntry.java
package com.quiz.app.dto;

public class LeaderBoardEntry {
    private String playerId;
    private String playerName;
    private int score;

    public LeaderBoardEntry(String playerId, String playerName, int score) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    @Override
    public String toString() {
        return "LeaderBoardEntry{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", score=" + score +
                '}';
    }
}
