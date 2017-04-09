package fr.nate.natebot.handlers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static fr.nate.natebot.domain.SlackResponse.inChannel;

@Component
public class PingHandler implements MessageHandler {

    @Override
    public Optional<SlackResponse> apply(SlackRequest request) {
        if (!request.getText().equals("ping")) {
            return Optional.empty();
        }
        return Optional.of(inChannel("pong!"));
    }
}
