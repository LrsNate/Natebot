package fr.nate.natebot.handlers;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SlackHandlerContainer {
    private final List<MessageHandler> handlers;
}
