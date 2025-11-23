package com.quiz.app.service;

import com.quiz.app.exception.InvalidCredentialsException;
import com.quiz.app.exception.UserAlreadyExistsException;
import com.quiz.app.exception.UserNotFoundException;
import com.quiz.app.model.User;
import com.quiz.app.repository.UserRepository;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;

  public UserService(
    UserRepository userRepository,
    AuthenticationManager authenticationManager,
    JWTService jwtService
  ) {
    this.userRepository = userRepository;
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
