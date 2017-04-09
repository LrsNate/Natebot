package fr.nate.natebot.handlers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PingHandlerTest {

    private PingHandler pingHandler;

    @Before
    public void setUp() {
        pingHandler = new PingHandler();
    }

    @Test
    public void testApply_withPingRequest() {
        SlackRequest request = SlackRequest.builder().text("ping").build();
        Optional<SlackResponse> response = pingHandler.apply(request);

        assertThat(response).contains(new SlackResponse("in_channel", "pong!"));
    }

    @Test
    public void testApply_withInvalidRequest() {
        SlackRequest request = SlackRequest.builder().text("foo").build();
        Optional<SlackResponse> response = pingHandler.apply(request);

        assertThat(response).isEmpty();
    }
}