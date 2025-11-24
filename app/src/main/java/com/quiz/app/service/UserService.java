
package com.quiz.app.service;

import com.quiz.app.dto.RoomHistoryDTO;
import com.quiz.app.exception.InvalidCredentialsException;
import com.quiz.app.exception.UserAlreadyExistsException;
import com.quiz.app.exception.UserNotFoundException;
import com.quiz.app.model.Player;
import com.quiz.app.model.Room;
import com.quiz.app.model.User;
import com.quiz.app.model.UserResponse;
import com.quiz.app.repository.RoomRepository;
import com.quiz.app.repository.UserRepository;
import com.quiz.app.repository.UserResponseRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final UserResponseRepository userResponseRepository;
  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;

  public UserService(
          UserRepository userRepository,
          RoomRepository roomRepository,
          UserResponseRepository userResponseRepository,
          AuthenticationManager authenticationManager,
          JWTService jwtService
  ) {
    this.userRepository = userRepository;
    this.roomRepository = roomRepository;
    this.userResponseRepository = userResponseRepository;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public User registerUser(User user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException(
              "User with username '" + user.getUsername() + "' already exists."
      );
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public List<RoomHistoryDTO> getParticipatedRooms(String username) {
    User user = findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

    Set<String> roomIds = user.getParticipatedRoomIds();
    if (roomIds == null || roomIds.isEmpty()) {
      return Collections.emptyList();
    }

    List<Room> rooms = (List<Room>) roomRepository.findAllById(roomIds);
    return rooms.stream().map(room -> {
      Optional<Player> playerOpt = room.getPlayers().values().stream()
              .filter(p -> user.getId().equals(p.getUserId()))
              .findFirst();

      List<UserResponse> userResponses = playerOpt.map(player -> userResponseRepository.findByPlayerIdAndRoomCode(player.getId(), room.getRoomCode()))
              .orElse(Collections.emptyList());

      return new RoomHistoryDTO(room.getRoomCode(), room.getTopic(), room.getTimer(), room.getNumberOfQuestions(), userResponses);
    }).collect(Collectors.toList());
  }

  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  public String verify(String username, String password) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password)
      );
      if (authentication.isAuthenticated()) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with username: " + username)
                );
        return jwtService.generateToken(user);
      }
    } catch (Exception e) {
      throw new InvalidCredentialsException("Invalid username or password");
    }
    throw new InvalidCredentialsException("Invalid username or password");
  }
}

