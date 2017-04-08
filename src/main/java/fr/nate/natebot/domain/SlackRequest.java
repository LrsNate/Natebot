package fr.nate.natebot.domain;

import lombok.Data;

@Data
public class SlackRequest {
    String token;
    String team_id;
    String team_domain;
    String channel_id;
    String channel_name;
    String user_id;
    String user_name;
    String command;
    String text;
    String response_url;
}
