package fr.nate.natebot.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SlackResponse {
    private String responseType;
    private String text;

    public static SlackResponse inChannel(String text) {
        return new SlackResponse("in_channel", text);
    }
}
