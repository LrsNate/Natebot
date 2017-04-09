package fr.nate.natebot.controllers;

import fr.nate.natebot.domain.SlackRequest;
import fr.nate.natebot.domain.SlackResponse;
import fr.nate.natebot.handlers.SlackMessageHandlerContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static fr.nate.natebot.domain.SlackResponse.inChannel;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SlackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlackMessageHandlerContainer container;

    @Test
    public void testNatebot() throws Exception {
        SlackRequest request = SlackRequest.builder().text("foo").build();
        SlackResponse response = inChannel("Ok!");
        given(container.apply(request)).willReturn(response);

        mockMvc.perform(post("/slack/natebot")
            .contentType(APPLICATION_FORM_URLENCODED)
            .param("text", "foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"response_type\": \"in_channel\", " +
                        "\"text\": \"Ok!\"}"));
    }
}