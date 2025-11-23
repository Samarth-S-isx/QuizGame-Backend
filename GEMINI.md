# Gemini CLI Context

## Environment
- **Date:** Sunday 23 November, 2025
- **Operating System:** win32
- **Project Temporary Directory:** C:\Users\samsa\.gemini\tmp\8b684ee6a39e6ef01f001476e8c07d1810c4f9beeba27d2f978b50b19ad9cce2
- **Current Working Directory:** c:\Users\samsa\Downloads\app

## Project Structure
```
c:\Users\samsa\Downloads\app\
├───.vscode\
│   └───settings.json
├───app\
│   ├───.mvn\
│   │   └───wrapper\
│   │       └───maven-wrapper.properties
│   ├───src\
│   │   ├───main\
│   │   │   ├───java\
│   │   │   │   └───com\
│   │   │   │       └───quiz\
│   │   │   │           └───app\
│   │   │   │               ├───QuizApplication.java
│   │   │   │               ├───config\
│   │   │   │               │   ├───GeminiConfig.java
│   │   │   │               │   ├───JwtFilter.java
│   │   │   │               │   ├───OpenAIConfig.java
│   │   │   │               │   ├───SecurityConfig.java
│   │   │   │               │   ├───WebSocketConfig.java
│   │   │   │               │   └───WebSocketEventListener.java
│   │   │   │               ├───controller\
│   │   │   │               │   ├───AnswerController.java
│   │   │   │               │   ├───QuestionController.java
│   │   │   │               │   ├───RoomController.java
│   │   │   │               │   └───UserController.java
│   │   │   │               ├───dto\
│   │   │   │               │   ├───LeaderBoardEntry.java
│   │   │   │               │   ├───PlayerJoinResponse.java
│   │   │   │               │   └───RoomSettingsDTO.java
│   │   │   │               ├───model\
│   │   │   │               │   ├───Answer.java
│   │   │   │               │   ├───Player.java
│   │   │   │               │   ├───Question.java
│   │   │   │               │   ├───Room.java
│   │   │   │               │   ├───User.java
│   │   │   │               │   ├───UserPrincipal.java
│   │   │   │               │   └───UserResponse.java
│   │   │   │               ├───repository\
│   │   │   │               │   ├───RoomRepository.java
│   │   │   │               │   ├───UserRepository.java
│   │   │   │               │   └───UserResponseRepository.java
│   │   │   │               └───service\
│   │   │   │                   ├───GameService.java
│   │   │   │                   ├───GeminiService.java
│   │   │   │                   ├───JWTService.java
│   │   │   │                   ├───MyUserDetailService.java
│   │   │   │                   ├───OpenAIService.java
│   │   │   │                   ├───QuestionService.java
│   │   │   │                   ├───RoomService.java
│   │   │   │                   └───UserService.java
│   │   │   └───resources\
│   │   │       ├───application.properties
│   │   │       ├───static\
│   │   │       └───templates\
│   │   └───test\
│   │       └───java\
│   │           └───com\
│   │               └───quiz\
│   │                   └───app\
│   │                       └───QuizApplicationTests.java
│   ├───target\
│   ├───mvnw
│   ├───mvnw.cmd
│   └───pom.xml
└───GEMINI.md
```