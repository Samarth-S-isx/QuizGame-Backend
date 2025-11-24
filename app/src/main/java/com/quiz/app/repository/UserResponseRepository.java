package com.quiz.app.repository;

import com.quiz.app.model.UserResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserResponseRepository extends MongoRepository<UserResponse, String> {
    List<UserResponse> findByPlayerIdAndRoomCode(String playerId, String roomCode);
}
