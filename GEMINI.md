# Gemini CLI Context

## Environment
- **Date:** Sunday 23 November, 2025
- **Operating System:** win32
- **Project Temporary Directory:** C:\Users\samsa\.gemini\tmp\8b684ee6a39e6ef01f001476e8c07d1810c4f9beeba27d2f978b50b19ad9cce2
- **Current Working Directory:** c:\Users\samsa\Downloads\app

## Project Structure
```
c:\Users\samsa\Downloads\app\
├───.gitignore
├───GEMINI.md
├───.git\
├───.vscode\
│   └───settings.json
└───app\
    ├───mvnw
    ├───mvnw.cmd
    ├───pom.xml
    ├───.mvn\
    │   └───wrapper\
    │       └───maven-wrapper.properties
    ├───src\
    │   ├───main\
    │   │   ├───java\
    │   │   │   └───com\
    │   │   │       └───quiz\
    │   │   │           └───app\
    │   │   │               ├───QuizApplication.java
    │   │   │               ├───config\
    │   │   │               │   ├───GeminiConfig.java
    │   │   │               │   ├───JwtFilter.java
    │   │   │               │   ├───OpenAIConfig.java
    │   │   │               │   ├───SecurityConfig.java
    │   │   │               │   ├───WebSocketConfig.java
    │   │   │               │   └───WebSocketEventListener.java
    │   │   │               ├───controller\
    │   │   │               │   ├───AnswerController.java
    │   │   │               │   ├───QuestionController.java
    │   │   │               │   ├───RoomController.java
    │   │   │               │   └───UserController.java
    │   │   │               ├───dto\
    │   │   │               │   ├───ErrorResponseDTO.java
    │   │   │               │   ├───LeaderBoardEntry.java
    │   │   │               │   ├───LoginRequestDTO.java
    │   │   │               │   ├───LoginResponseDTO.java
    │   │   │               │   ├───PlayerJoinResponse.java
    │   │   │               │   ├───RegisterRequestDTO.java
    │   │   │               │   ├───RoomSettingsDTO.java
    │   │   │               │   └───UserDTO.java
    │   │   │               ├───exception\
    │   │   │               │   ├───GlobalExceptionHandler.java
    │   │   │               │   ├───InvalidCredentialsException.java
    │   │   │               │   ├───InvalidJwtException.java
    │   │   │               │   ├───JwtInitializationException.java
    │   │   │               │   ├───PlayerNotFoundException.java
    │   │   │               │   ├───QuestionGenerationException.java
    │   │   │               │   ├───QuestionParseException.java
    │   │   │               │   ├───RoomAlreadyExistsException.java
    │   │   │               │   ├───RoomNotFoundException.java
    │   │   │               │   ├───UserAlreadyExistsException.java
    │   │   │               │   └───UserNotFoundException.java
    │   │   │               ├───model\
    │   │   │               │   ├───Answer.java
    │   │   │               │   ├───Player.java
    │   │   │               │   ├───Question.java
    │   │   │               │   ├───Room.java
    │   │   │               │   ├───User.java
    │   │   │               │   ├───UserPrincipal.java
    │   │   │               │   └───UserResponse.java
    │   │   │               ├───repository\
    │   │   │               │   ├───RoomRepository.java
    │   │   │               │   ├───UserRepository.java
    │   │   │               │   └───UserResponseRepository.java
    │   │   │               └───service\
    │   │   │                   ├───GameService.java
    │   │   │                   ├───GeminiService.java
    │   │   │                   ├───JWTService.java
    │   │   │                   ├───MyUserDetailService.java
    │   │   │                   ├───OpenAIService.java
    │   │   │                   ├───QuestionService.java
    │   │   │                   ├───RoomService.java
    │   │   │                   └───UserService.java
    │   │   └───resources\
    │   │       ├───static\
    │   │       └───templates\
    │   └───test\
    │       └───java\
    │           └───com\
    │               └───quiz\
    │                   └───app\
    │                       └───QuizApplicationTests.java
    └───target\
        ├───classes\
        │   └───com\
        │       └───quiz\
        │           └───app\
        │               ├───QuizApplication.class
        │               ├───config\
        │               │   ├───GeminiConfig.class
        │               │   ├───JwtFilter.class
        │               │   ├───OpenAIConfig.class
        │               │   ├───SecurityConfig.class
        │               │   ├───WebSocketConfig.class
        │               │   └───WebSocketEventListener.class
        │               ├───controller\
        │               │   ├───AnswerController.class
        │               │   ├───QuestionController.class
        │               │   ├───RoomController.class
        │               │   └───UserController.class
        │               ├───dto\
        │               │   ├───ErrorResponseDTO.class
        │               │   ├───LeaderBoardEntry.class
        │               │   ├───LoginRequestDTO.class
        │               │   ├───LoginResponseDTO.class
        │               │   ├───PlayerJoinResponse.class
        │               │   ├───RegisterRequestDTO.class
        │               │   ├───RoomSettingsDTO.class
        │               │   └───UserDTO.class
        │               ├───exception\
        │               │   ├───GlobalExceptionHandler.class
        │               │   ├───InvalidCredentialsException.class
        │               │   ├───InvalidJwtException.class
        │               │   ├───JwtInitializationException.class
        │               │   ├───PlayerNotFoundException.class
        │               │   ├───QuestionGenerationException.class
        │               │   ├───QuestionParseException.class
        │               │   ├───RoomAlreadyExistsException.class
        │               │   ├───RoomNotFoundException.class
        │               │   ├───UserAlreadyExistsException.class
        │               │   └───UserNotFoundException.class
        │               ├───model\
        │               │   ├───Answer.class
        │               │   ├───Player.class
        │               │   ├───Question.class
        │               │   ├───Room.class
        │               │   ├───User.class
        │               │   ├───UserPrincipal.class
        │               │   └───UserResponse.class
        │               ├───repository\
        │               │   ├───RoomRepository.class
        │               │   ├───UserRepository.class
        │               │   └───UserResponseRepository.class
        │               └───service\
        │                   ├───GameService.class
        │                   ├───GeminiService.class
        │                   ├───JWTService.class
        │                   ├───MyUserDetailService.class
        │                   ├───OpenAIService.class
        │                   ├───QuestionService.class
        │                   ├───RoomService.class
        │                   └───UserService.class
        ├───generated-sources\
        │   └───annotations\
        ├───generated-test-sources\
        │   └───test-annotations\
        ├───maven-archiver\
        ├───maven-status\
        │   └───maven-compiler-plugin\
        │       ├───compile\
        │       │   └───default-compile\
        │       └───testCompile\
        │           └───default-testCompile\
        ├───surefire-reports\
        └───test-classes\
            └───com\
                └───quiz\
                    └───app\
                        └───QuizApplicationTests.class
```