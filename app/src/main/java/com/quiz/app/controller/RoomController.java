package com.quiz.app.controller;

import com.quiz.app.dto.PlayerJoinResponse;
import com.quiz.app.dto.RoomSettingsDTO;
import com.quiz.app.model.Player;
import com.quiz.app.model.Room;
import com.quiz.app.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5173"})
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Endpoint to create a new room.
    @PostMapping("/create")    
    public Room createRoom(@RequestParam String roomCode,@RequestBody RoomSettingsDTO settings) {
        // System.out.println(settings.getNumQuestions()+settings.getTopic()+settings.getTimePerQuestion());
        return roomService.createRoom(roomCode,settings);
    }

    // Endpoint to join an existing room.
    // Example: POST /rooms/join?roomCode=123456&playerName=Sam
    @PostMapping("/join")
    public PlayerJoinResponse joinRoom(@RequestParam String roomCode,
                                     @RequestParam(required = false) String playerName,
                                     Authentication authentication) {
        Player player = roomService.joinRoom(roomCode, playerName, authentication);
        return new PlayerJoinResponse(roomCode, player.getId(), player.getName(),10);
    }

    // Endpoint to start the game.
    // When invoked by the room creator, questions are broadcast every 10 seconds.
    @PostMapping("/start")
    public String startGame(@RequestParam String roomCode) {
        try {
            roomService.startGame(roomCode);
            return "Game started for room: " + roomCode;
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
