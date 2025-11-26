package com.quiz.app.service;

import com.quiz.app.dto.RoomSettingsDTO;
import com.quiz.app.exception.RoomAlreadyExistsException;
import com.quiz.app.exception.RoomNotFoundException;
import com.quiz.app.exception.UserNotFoundException;
import com.quiz.app.model.Answer;
import com.quiz.app.model.Player;
import com.quiz.app.model.Question;
import com.quiz.app.model.Room;
import com.quiz.app.model.User;
import com.quiz.app.repository.RoomRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
  private final RoomRepository roomRepository;
  private final GameService gameService;
  private final QuestionService questionService;
  private final UserService userService;
  private final SimpMessagingTemplate messagingTemplate;

  @Autowired
  public RoomService(RoomRepository roomRepository,GameService gameService,QuestionService questionService,UserService userService,SimpMessagingTemplate messagingTemplate) {
    this.roomRepository = roomRepository;
    this.gameService = gameService;
    this.questionService = questionService;
    this.userService = userService;
    this.messagingTemplate = messagingTemplate;
  }

  public Room createRoom(String roomCode, RoomSettingsDTO settingsDTO) {
    if (roomRepository.findById(roomCode).isPresent()) {
      throw new RoomAlreadyExistsException(
        "Room with code '" + roomCode + "' already exists."
      );
    }
    // System.out.println("Creating room with code: " + roomCode);
    Room room = new Room(roomCode);
    room.setNumberOfQuestions(settingsDTO.getNumQuestions());
    room.setTopic(settingsDTO.getTopic());
    room.setTimer(settingsDTO.getTimePerQuestion());
    room.setDifficulty(settingsDTO.getDifficulty());
    List<Question> questions = questionService.generateQuestions(
      room.getTopic(),
      room.getNumberOfQuestions(),
      room.getDifficulty()
    );
    room.setQuestions(questions);

    return roomRepository.save(room);
  }

  public Player joinRoom(String roomCode,String playerName,Authentication authentication) {
    System.out.println(playerName);
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

      User user = userService
        .findByUsername(username)
        .orElseThrow(() ->
          new UserNotFoundException("Authenticated user not found in database")
        );

      user.getParticipatedRoomIds().add(roomCode);
      userService.saveUser(user);

      player = room.addPlayer(finalPlayerName, user.getId());
    } else {
      finalPlayerName =
        (playerName != null && !playerName.trim().isEmpty())
          ? playerName
          : "Anonymous";
      player = room.addPlayer(finalPlayerName);
    }

    roomRepository.save(room); // Save the room with the new player
    // System.out.println(room.getPlayers());
    List<String> players= room.getPlayers().values().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    // players.values().stream()
    //             .map(Player::getName)
    //             .collect(Collectors.toList());
    gameService.notifyPlayerJoin(roomCode, finalPlayerName, players,room.getTopic());
    // messagingTemplate.convertAndSend(
    //   "/topic/" + roomCode + "/players",
    //   room.getPlayers()
    // );
    return player;
  }

  public void startGame(String roomCode) {
    gameService.startGame(roomCode);
  }

  public void handleAnswer(Answer answer) {
    gameService.handleAnswer(answer);
  }
}
