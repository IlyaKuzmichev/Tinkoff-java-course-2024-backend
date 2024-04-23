package edu.java.controller;

import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
public class ChatControllerTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterChatSuccess() throws Exception {
        Long chatId = 123L;
        Mockito.doNothing().when(userService).addUser(new User(chatId, null));
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/%d".formatted(chatId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteChatSuccess() throws Exception {
        Long chatId = 456L;
        Mockito.doNothing().when(userService).removeUser(chatId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/%d".formatted(chatId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
