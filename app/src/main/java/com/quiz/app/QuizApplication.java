package com.quiz.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.quiz.app.config.OpenAIConfig;

@SpringBootApplication
// @EnableConfigurationProperties(OpenAIConfig.class)
public class QuizApplication {

	public static void main(String[]  args) {
		SpringApplication.run(QuizApplication.class, args);
	}

}
