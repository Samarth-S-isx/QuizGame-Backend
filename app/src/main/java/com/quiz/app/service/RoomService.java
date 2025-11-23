package com.quiz.app.service;

import com.quiz.app.dto.RoomSettingsDTO;
import com.quiz.app.exception.RoomAlreadyExistsException;
import com.quiz.app.exception.RoomNotFoundException;
import com.quiz.app.model.Answer;
import com.quiz.app.model.Player;
import com.quiz.app.model.Question;
import com.quiz.app.model.Room;
import com.quiz.app.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final QuestionService questionService;

    @Autowired
    public RoomService(RoomRepository roomRepository, GameService gameService, QuestionService questionService) {
        this.roomRepository = roomRepository;
        this.gameService = gameService;
        this.questionService = questionService;
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

    public Player joinRoom(String roomCode, String playerName) {
        Room room = roomRepository
            .findById(roomCode)
            .orElseThrow(() ->
                new RoomNotFoundException("Room with code '" + roomCode + "' not found.")
            );
        Player player = room.addPlayer(playerName);
        roomRepository.save(room); // Save the room with the new player
        gameService.notifyPlayerJoin(roomCode, playerName);
        return player;
    }

    public void startGame(String roomCode) {
        gameService.startGame(roomCode);
    }

    public void handleAnswer(Answer answer) {
        gameService.handleAnswer(answer);
    }
}
