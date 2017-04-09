package fr.nate.natebot.handlers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;

import java.util.Optional;
import java.util.function.Function;

public interface MessageHandler extends Function<SlackRequest, Optional<SlackResponse>> {
}
