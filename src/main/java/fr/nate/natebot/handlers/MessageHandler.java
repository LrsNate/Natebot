package fr.nate.natebot.handlers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;

import java.util.Optional;

public interface MessageHandler {
    Optional<SlackResponse> handle(SlackRequest request);
}
