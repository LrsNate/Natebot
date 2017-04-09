package fr.nate.natebot.handlers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static fr.nate.natebot.domain.SlackResponse.inChannel;

@RequiredArgsConstructor
public class SlackMessageHandlerContainer {
    private final List<MessageHandler> handlers;

    public SlackResponse apply(SlackRequest request) {
        for (MessageHandler handler : handlers) {
            Optional<SlackResponse> response = handler.apply(request);
            if (response.isPresent()) {
                return response.get();
            }
        }
        return inChannel("...I'm sorry, what was that?");
    }
}
