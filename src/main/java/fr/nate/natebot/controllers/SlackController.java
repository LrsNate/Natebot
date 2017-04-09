package fr.nate.natebot.controllers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import fr.nate.natebot.handlers.SlackMessageHandlerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlackController {

    @Autowired
    private SlackMessageHandlerContainer slackMessageHandlerContainer;

    @PostMapping("/slack/natebot")
    public SlackResponse natebot(@ModelAttribute SlackRequest request) {
        return slackMessageHandlerContainer.apply(request);
    }
}
