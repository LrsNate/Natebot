package fr.nate.natebot.controllers.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Status {
    private final String status;
}
