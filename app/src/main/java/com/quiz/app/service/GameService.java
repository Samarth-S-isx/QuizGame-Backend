package com.quiz.app.service;

import com.quiz.app.exception.PlayerNotFoundException;
import com.quiz.app.exception.RoomNotFoundException;
import com.quiz.app.model.Answer;
import com.quiz.app.model.Player;
import com.quiz.app.model.Question;
import com.quiz.app.model.Room;
import com.quiz.app.model.UserResponse;
import com.quiz.app.repository.RoomRepository;
import com.quiz.app.repository.UserResponseRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  private final SimpMessagingTemplate messagingTemplate;
  private final RoomRepository roomRepository;
  private final UserResponseRepository userResponseRepository;
  private final Map<String, List<String>> answeredPlayersPerQuestion = new HashMap<>();

  @Autowired
  public GameService(
    SimpMessagingTemplate messagingTemplate,
    RoomRepository roomRepository,
    UserResponseRepository userResponseRepository
  ) {
    this.messagingTemplate = messagingTemplate;
    this.roomRepository = roomRepository;
    this.userResponseRepository = userResponseRepository;
  }

  public void startGame(String roomCode) {
    // System.out.println("roomCode");
    Room room = roomRepository
      .findById(roomCode)
      .orElseThrow(() ->
        new RoomNotFoundException("Room with code '" + roomCode + "' not found.")
      );
    if (room.getQuestions() == null) {
      throw new RuntimeException("Room not found");
    }
    List<Question> questions = room.getQuestions();

    // Send initial leaderboard
    messagingTemplate.convertAndSend(
      "/topic/" + roomCode + "/leaderboard",
      room.getLeaderboard()
    );
    ScheduledExecutorService scheduler =
      Executors.newSingleThreadScheduledExecutor();
    AtomicInteger index = new AtomicInteger(0);

    Runnable task = () -> {
      int currentIndex = index.getAndIncrement();

      // Handle unanswered questions for the previous question
      if (currentIndex > 0) {
        int previousQuestionIndex = currentIndex - 1;
        handleUnanswered(roomCode, previousQuestionIndex);
      }

      if (currentIndex < questions.size()) {
        // Fetch the latest room state before sending updates
        Room currentRoom = roomRepository.findById(roomCode).orElse(null);
        if (currentRoom == null) {
          scheduler.shutdown();
          return;
        }
        answeredPlayersPerQuestion.put(roomCode + ":" + currentIndex, Collections.synchronizedList(new java.util.ArrayList<>()));
        Question q = questions.get(currentIndex);
        Map<String, Object> payload = new HashMap<>();
        payload.put("question", q);
        payload.put("index", currentIndex);
        // System.out.println(currentIndex);

        messagingTemplate.convertAndSend(
          "/topic/" + roomCode + "/question",
          payload
        );
        messagingTemplate.convertAndSend(
          "/topic/" + roomCode + "/leaderboard",
          currentRoom.getLeaderboard()
        );
      } else {
        Room finalRoom = roomRepository.findById(roomCode).orElse(null);
        messagingTemplate.convertAndSend(
          "/topic/" + roomCode + "/leaderboard",
          finalRoom != null ? finalRoom.getLeaderboard() : List.of()
        );
        answeredPlayersPerQuestion.entrySet().removeIf(entry -> entry.getKey().startsWith(roomCode + ":"));
        // System.out.println(room.getLeaderboard());
        messagingTemplate.convertAndSend("/topic/" + roomCode + "/game", "Game Over");
        scheduler.shutdown();
      }
    };
    // Send the first question immediately, then based on the room's timer.
    scheduler.scheduleAtFixedRate(task, 0, room.getTimer(), TimeUnit.SECONDS);
  }

  public void handleAnswer(Answer payload) {
    // System.out.println(
    //   "answer submitted" + payload.getPlayerId() + payload.getSelectedOption()
    // );
    Room room = roomRepository
      .findById(payload.getRoomCode())
      .orElseThrow(() ->
        new RoomNotFoundException(
          "Room with code '" + payload.getRoomCode() + "' not found."
        )
      );

    int questionIndex = payload.getQuestionIndex();
    Player player = room.getPlayer(payload.getPlayerId());

    if (player == null) {
      throw new PlayerNotFoundException(
        "Player with id '" + payload.getPlayerId() + "' not found in room."
      );
    }

    String questionKey = payload.getRoomCode() + ":" + questionIndex;
    List<String> answeredPlayers = answeredPlayersPerQuestion.get(questionKey);
    if (answeredPlayers == null || answeredPlayers.contains(player.getId())) return;
    answeredPlayers.add(player.getId());

    Question question = room.getQuestions().get(questionIndex);
    int selectedOption = payload.getSelectedOption();
    boolean isCorrect = selectedOption == question.getCorrectAnswer();

    if (isCorrect) {
      room.incrementScore(player, 10);
    }

    // Save the response
    UserResponse response = new UserResponse();
    response.setRoomCode(payload.getRoomCode());
    response.setPlayerId(player.getId());
    response.setQuestionIndex(questionIndex);
    response.setQuestionText(question.getQuestionText());
    response.setOptions(question.getOptions());
    response.setCorrectAnswerIndex(question.getCorrectAnswer());
    response.setSelectedOption(selectedOption);
    response.setCorrect(isCorrect);
    response.setAnsweredAt(LocalDateTime.now());

    userResponseRepository.save(response);
    roomRepository.save(room); // Persist the changes to the room
  }

  private void handleUnanswered(String roomCode, int questionIndex) {
    Room room = roomRepository.findById(roomCode).orElse(null);
    if (room == null) {
      return;
    }

    String questionKey = roomCode + ":" + questionIndex;
    List<String> playersWhoAnswered = answeredPlayersPerQuestion.getOrDefault(questionKey, List.of());
    Question question = room.getQuestions().get(questionIndex);

    List<Player> allPlayers = room.getPlayers().values().stream().collect(Collectors.toList());

    for (Player player : allPlayers) {
      if (!playersWhoAnswered.contains(player.getId())) {
        // This player did not answer
        // UserResponse unansweredResponse = new UserResponse(player.getId(), roomCode, questionIndex, -1, false);
        UserResponse response = new UserResponse();
        response.setRoomCode(roomCode);
        response.setPlayerId(player.getId());
        response.setQuestionIndex(questionIndex);
        response.setQuestionText(question.getQuestionText());
        response.setOptions(question.getOptions());
        response.setCorrectAnswerIndex(question.getCorrectAnswer());
        response.setSelectedOption(5);
        response.setCorrect(false);

        response.setQuestionText(question.getQuestionText());
        userResponseRepository.save(response);
      }
    }
  }

  public void notifyPlayerJoin(String roomCode, String playerName,List<String> players,String topic) {
    Map<String, Object> response = Map.of(
        "type", "PLAYER_JOINED",  // Helpful for frontend to switch logic
        "message", playerName + " has joined the room!",
        "players", players,
        "topic",topic
    );

    messagingTemplate.convertAndSend(
        "/topic/room/" + roomCode + "/notifications",
        response
    );
  }
}
