package com.quiz.app.controller;
import com.quiz.app.dto.LoginRequestDTO;
import com.quiz.app.dto.LoginResponseDTO;
import com.quiz.app.dto.RegisterRequestDTO;
import com.quiz.app.dto.UserDTO;
import com.quiz.app.model.User;
import com.quiz.app.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public UserDTO registerUser(@RequestBody RegisterRequestDTO registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setPassword(registerRequest.getPassword());
    user.setRole(registerRequest.getRole());
    User registeredUser = userService.registerUser(user);
    return new UserDTO(
      registeredUser.getId(),
      registeredUser.getUsername(),
      registeredUser.getRole()
    );
  }

  @GetMapping("/user")
  public List<UserDTO> getUser() {
    return userService
      .getAllUser()
      .stream()
      .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getRole())
      )
      .collect(Collectors.toList());
  }

  @PostMapping("/login")
  public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
    String token = userService.verify(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );
    return new LoginResponseDTO(token);
  }
}
