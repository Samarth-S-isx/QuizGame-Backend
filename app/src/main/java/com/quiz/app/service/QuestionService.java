package com.quiz.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.app.exception.QuestionGenerationException;
import com.quiz.app.exception.QuestionParseException;
import com.quiz.app.model.Question;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  private final GeminiService geminiService;
  private final OpenAIService openAIService;

  @Autowired
  public QuestionService(
    GeminiService geminiService,
    OpenAIService openAIService
  ) {
    this.geminiService = geminiService;
    this.openAIService = openAIService;
  }

  public List<Question> generateQuestions(String topic, int numberOfQuestions,String difficulty) {
    try {
      String response = geminiService.generateQuestions(topic, numberOfQuestions,difficulty).block();

//       String response = """
// {
//   "candidates": [
//     {
//       "content": {
//         "parts": [
//           {
//             "text": "```json\\n[\\n  {\\n    \\"questionText\\": \\"Which planet is known as the 'Red Planet'?\\",\\n    \\"options\\": [\\"Venus\\", \\"Mars\\", \\"Jupiter\\", \\"Saturn\\"],\\n    \\"correctAnswer\\": 1\\n  },\\n  {\\n    \\"questionText\\": \\"What is the capital city of Australia?\\",\\n    \\"options\\": [\\"Sydney\\", \\"Melbourne\\", \\"Canberra\\", \\"Brisbane\\"],\\n    \\"correctAnswer\\": 2\\n  },\\n  {\\n    \\"questionText\\": \\"Who painted the Mona Lisa?\\",\\n    \\"options\\": [\\"Vincent van Gogh\\", \\"Leonardo da Vinci\\", \\"Michelangelo\\", \\"Raphael\\"],\\n    \\"correctAnswer\\": 1\\n  },\\n  {\\n    \\"questionText\\": \\"What is the chemical symbol for water?\\",\\n    \\"options\\": [\\"CO2\\", \\"O2\\", \\"H2O\\", \\"NaCl\\"],\\n    \\"correctAnswer\\": 2\\n  },\\n  {\\n    \\"questionText\\": \\"In what year did World War II end?\\",\\n    \\"options\\": [\\"1943\\", \\"1945\\", \\"1947\\", \\"1949\\"],\\n    \\"correctAnswer\\": 1\\n  }\\n]\\n```"
//           }
//         ],
//         "role": "model"
//       },
//       "finishReason": "STOP",
//       "citationMetadata": {
//         "citationSources": [
//           {
//             "startIndex": 19,
//             "endIndex": 192,
//             "uri": "https://github.com/LovingBrother/quizApp"
//           }
//         ]
//       },
//       "avgLogprobs": -0.038273578381720387
//     }
//   ],
//   "usageMetadata": {
//     "promptTokenCount": 92,
//     "candidatesTokenCount": 262,
//     "totalTokenCount": 354,
//     "promptTokensDetails": [
//       {
//         "modality": "TEXT",
//         "tokenCount": 92
//       }
//     ],
//     "candidatesTokensDetails": [
//       {
//         "modality": "TEXT",
//         "tokenCount": 262
//       }
//     ]
//   },
//   "modelVersion": "gemini-2.0-flash-lite"
// }
// """;
      return parseQuestionsFromGemini(response);
    } catch (QuestionParseException e) {
      throw new QuestionGenerationException(
        "Failed to generate questions for topic: " + topic,
        e
      );
    }
  }

  private List<Question> getDefaultQuestions() {
    List<Question> questions = new ArrayList<>();
    questions.add(
      new Question(
        "What is the capital of France?",
        Arrays.asList("Berlin", "Paris", "Madrid", "London"),
        0
      )
    );
    questions.add(
      new Question(
        "Which planet is known as the Red Planet?",
        Arrays.asList("Earth", "Mars", "Jupiter", "Venus"),
        1
      )
    );
    return questions;
  }

  private List<Question> parseQuestionsFromGemini(String geminiResponse)
    throws QuestionParseException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(geminiResponse);
      JsonNode textNode = root
        .path("candidates")
        .get(0)
        .path("content")
        .path("parts")
        .get(0)
        .path("text");

      if (textNode == null || textNode.isMissingNode()) {
        throw new QuestionParseException(
          "Invalid Gemini response format. Missing text content."
        );
      }

      String rawText = textNode.asText().trim();
      String cleanedJson = rawText
        .replaceAll("^```json\\s*|^```\\s*", "")
        .replaceAll("\\s*```$", "")
        .trim();
      JsonNode questionsJson = mapper.readTree(cleanedJson);
      List<Question> questions = new ArrayList<>();

      if (!questionsJson.isArray()) {
        throw new QuestionParseException(
          "Invalid JSON format: Expected an array."
        );
      }

      for (JsonNode q : questionsJson) {
        String questionText = q.has("questionText")
          ? q.get("questionText").asText()
          : "Unknown question";
        List<String> options = new ArrayList<>();

        JsonNode optionsNode = q.get("options");
        if (optionsNode != null && optionsNode.isArray()) {
          for (JsonNode opt : optionsNode) {
            options.add(opt.asText());
          }
        }

        int correctIndex = q.has("correctAnswer")
          ? q.get("correctAnswer").asInt()
          : -1;
        questions.add(new Question(questionText, options, correctIndex));
      }

      return questions;
    } catch (IOException e) {
      throw new QuestionParseException(
        "Error parsing questions from Gemini response",
        e
      );
    }
  }

  private List<Question> parseQuestionsFromOpenAI(String openAIResponse)
    throws QuestionParseException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(openAIResponse);
      String content = root
        .path("choices")
        .get(0)
        .path("message")
        .path("content")
        .asText();

      // Parse the content string as a JSON array
      JsonNode questionsJson = mapper.readTree(content);
      List<Question> questions = new ArrayList<>();

      for (JsonNode q : questionsJson) {
        String questionText = q.get("questionText").asText();
        List<String> options = new ArrayList<>();
        for (JsonNode opt : q.get("options")) {
          options.add(opt.asText());
        }
        int correctIndex = q.get("correctAnswer").asInt();

        questions.add(new Question(questionText, options, correctIndex));
      }

      return questions;
    } catch (IOException e) {
      throw new QuestionParseException(
        "Error parsing questions from OpenAI response",
        e
      );
    }
  }
}
