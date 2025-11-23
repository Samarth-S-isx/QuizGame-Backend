package com.quiz.app.repository;

import com.quiz.app.model.UserResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResponseRepository extends MongoRepository<UserResponse, String> {
}
