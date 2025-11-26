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

    @PostMapping("/create")    
    public Room createRoom(@RequestParam String roomCode,@RequestBody RoomSettingsDTO settings) {
        System.out.println(settings.getNumQuestions()+settings.getTopic()+settings.getTimePerQuestion());
        return roomService.createRoom(roomCode,settings);
    }

    @PostMapping("/join")
    public PlayerJoinResponse joinRoom(@RequestParam String roomCode,
                                     @RequestParam(required = false) String playerName,
                                     Authentication authentication) {
        Player player = roomService.joinRoom(roomCode, playerName, authentication);
        return new PlayerJoinResponse(roomCode, player.getId(), player.getName(),10);
    }

    // Endpoint to start the game.
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
