package com.quiz.app.service;

import com.quiz.app.dto.RoomSettingsDTO;
import com.quiz.app.exception.RoomAlreadyExistsException;
import com.quiz.app.exception.RoomNotFoundException;
import com.quiz.app.exception.UserNotFoundException;
import com.quiz.app.model.*;
import com.quiz.app.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    public RoomService(RoomRepository roomRepository, GameService gameService, QuestionService questionService, UserService userService) {
        this.roomRepository = roomRepository;
        this.gameService = gameService;
        this.questionService = questionService;
        this.userService = userService;
    }

    public Room createRoom(String roomCode, RoomSettingsDTO settingsDTO) {
        if (roomRepository.findById(roomCode).isPresent()) {
            throw new RoomAlreadyExistsException(
                "Room with code '" + roomCode + "' already exists."
            );
        }
        System.out.println("Creating room with code: " + roomCode);
        Room room = new Room(roomCode);
        room.setNumberOfQuestions(settingsDTO.getNumQuestions());
        room.setTopic(settingsDTO.getTopic());
        room.setTimer(settingsDTO.getTimePerQuestion());

        List<Question> questions = questionService.generateQuestions(room.getTopic(), room.getNumberOfQuestions());
        room.setQuestions(questions);

        return roomRepository.save(room);
    }

    public Player joinRoom(String roomCode, String playerName, Authentication authentication) {
        Room room = roomRepository
            .findById(roomCode)
            .orElseThrow(() ->
                new RoomNotFoundException("Room with code '" + roomCode + "' not found.")
            );

        String finalPlayerName;
        Player player;

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            finalPlayerName = username;

            User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found in database"));
            
            user.getParticipatedRoomIds().add(roomCode);
            userService.saveUser(user);
            
            player = room.addPlayer(finalPlayerName, user.getId());
        } else {
            finalPlayerName = (playerName != null && !playerName.trim().isEmpty()) ? playerName : "Anonymous";
            player = room.addPlayer(finalPlayerName);
        }

        roomRepository.save(room); // Save the room with the new player
        gameService.notifyPlayerJoin(roomCode, finalPlayerName);
        return player;
    }

    public void startGame(String roomCode) {
        gameService.startGame(roomCode);
    }

    public void handleAnswer(Answer answer) {
        gameService.handleAnswer(answer);
    }
}
