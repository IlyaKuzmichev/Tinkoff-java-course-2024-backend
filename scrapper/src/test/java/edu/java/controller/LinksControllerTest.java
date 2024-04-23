package edu.java.controller;

import edu.java.models.Link;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
public class LinksControllerTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @Test
    public void testGetAllLinksSuccess() throws Exception {
        Long chatId = 123L;
        Mockito.doReturn(new ArrayList<Link>(List.of())).when(linkService).findAllLinksForUser(chatId);

        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddLinkSuccess() throws Exception {
        Long chatId = 1L;
        URI link = URI.create("example.com");
        String requestBody = "{\"link\": \"example.com\"}";
        Mockito.doReturn(new Link(123L, link)).when(linkService).addLink(chatId, link);

        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRemoveLinkSuccess() throws Exception {
        Long chatId = 1L;
        URI link = URI.create("example.com");
        String requestBody = "{\"link\": \"example.com\"}";
        Mockito.doReturn(new Link(123L, link)).when(linkService).removeLinkByURL(chatId, link);

        mockMvc.perform(MockMvcRequestBuilders.delete("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
