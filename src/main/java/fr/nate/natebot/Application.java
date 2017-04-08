package fr.nate.natebot;

import fr.nate.natebot.handlers.SlackHandlerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SlackHandlerContainer slackHandlerContainer() {
        return null;
    }
}
