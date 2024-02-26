package edu.java.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class LinksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllLinksSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", "123")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(123))
            .andExpect(jsonPath("$.links[0].url").value("aboba.com"))
            .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    public void testAddLinkSuccess() throws Exception {
        String requestBody = "{\"link\": \"example.com\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", "123")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(123))
            .andExpect(jsonPath("$.url").value("example.com"));
    }

    @Test
    public void testRemoveLink_Success() throws Exception {
        String requestBody = "{\"link\": \"example.com\"}";

        mockMvc.perform(MockMvcRequestBuilders.delete("/links")
                .header("Tg-Chat-Id", "123")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(123))
            .andExpect(jsonPath("$.url").value("example.com"));
    }
}
