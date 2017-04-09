package fr.nate.natebot.handlers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static fr.nate.natebot.domain.SlackResponse.inChannel;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class SlackMessageHandlerContainerTest {

    private SlackMessageHandlerContainer container;

    private MessageHandler dummyHandler = (request) -> {
        if (!request.getText().equals("foo")) {
            return Optional.empty();
        }
        return Optional.of(inChannel("bar"));
    };

    @Before
    public void setUp() {
        container = new SlackMessageHandlerContainer(singletonList(dummyHandler));
    }

    @Test
    public void testApply_withKnownRequest() {
        SlackRequest request = SlackRequest.builder().text("foo").build();
        SlackResponse response = container.apply(request);

        assertThat(response).isEqualTo(new SlackResponse("in_channel", "bar"));
    }

    @Test
    public void testApply_withUnknownRequest() {
        SlackRequest request = SlackRequest.builder().text("abcde").build();
        SlackResponse response = container.apply(request);

        assertThat(response)
                .isEqualTo(new SlackResponse("in_channel", "...I'm sorry, what was that?"));
    }
}