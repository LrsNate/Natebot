package fr.nate.natebot.controllers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.nate.natebot.domain.SlackResponse.inChannel;

@RestController
public class SlackController {

    @PostMapping("/slack/natebot")
    public SlackResponse natebot(@ModelAttribute SlackRequest request) {
        System.out.println(request);
        return inChannel("foo");
    }
}
