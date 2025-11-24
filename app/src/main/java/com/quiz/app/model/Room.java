package com.quiz.app.model;

import com.quiz.app.dto.LeaderBoardEntry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Document(collection = "rooms")
public class Room {
    @Id
    private String roomCode;
    private Map<String, Player> players;
    private String topic;
    private Integer timer;
    private Integer numberOfQuestions;
    private List<Question> questions;
    private Map<String, LeaderBoardEntry> leaderboard;

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

    public Room(String roomCode) {
        this.roomCode = roomCode;
        this.players = new ConcurrentHashMap<>();
        this.questions = new ArrayList<>();
        this.leaderboard = new ConcurrentHashMap<>();
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String id) {
        return players.get(id);
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Player addPlayer(String playerName) {
        return addPlayer(playerName, null);
    }

    public Player addPlayer(String playerName, String userId) {
        Player player = new Player(playerName);
        if (userId != null) {
            player.setUserId(userId);
        }
        players.put(player.getId(), player);
        leaderboard.put(player.getId(), new LeaderBoardEntry(player.getId(), player.getName(), 0));
        return player;
    }

    public List<LeaderBoardEntry> getLeaderboard() {
        System.out.println(new ArrayList<>(leaderboard.values()));
        return new ArrayList<>(leaderboard.values());
    }

    public void incrementScore(Player player, int score) {
        System.out.println(player.getName()+"  "+score);
        leaderboard.computeIfPresent(player.getId(), (playerId, entry) -> {
            entry.setScore(entry.getScore() + score);
            return entry;
        });
        
    }
}
