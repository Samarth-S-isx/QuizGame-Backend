package com.quiz.app.controller;
import com.quiz.app.model.Answer;
import com.quiz.app.model.Room;
import com.quiz.app.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5173"})
@RestController
public class AnswerController {
    @Autowired
    private RoomService roomService;

    @MessageMapping("/answer")
    public void submitAnswer(@Payload Answer answerPayload) {
        
        roomService.handleAnswer(answerPayload);
    }
}

