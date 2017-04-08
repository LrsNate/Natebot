package fr.nate.natebot;

import fr.nate.natebot.handlers.PingHandler;
import fr.nate.natebot.handlers.SlackMessageHandlerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.util.Collections.singletonList;

@SpringBootApplication
public class Application {

    @Autowired
    private PingHandler pingHandler;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SlackMessageHandlerContainer slackMessageHandlerContainer() {
        return new SlackMessageHandlerContainer(singletonList(pingHandler));
    }
}
