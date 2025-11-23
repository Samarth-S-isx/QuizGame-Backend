// PlayerJoinResponse.java
package com.quiz.app.dto;

public class PlayerJoinResponse {
    private String roomCode;
    private String playerId;
    private String playerName;
    private Integer timer;

    public PlayerJoinResponse(String roomCode, String playerId, String playerName,Integer timer) {
        this.roomCode = roomCode;
        this.playerId = playerId;
        this.playerName = playerName;
        this.timer = timer;
    }

    // Getters
    public String getRoomCode() {
        return roomCode;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
