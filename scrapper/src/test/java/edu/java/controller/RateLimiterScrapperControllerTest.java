package edu.java.controller;

import edu.java.models.Link;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource(locations = "classpath:test")
public class RateLimiterScrapperControllerTest extends IntegrationEnvironment {
    @Autowired
    private MockMvc mockMvc;
    @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}")
    private Integer chatRateLimit;
    @Value("${bucket4j.filters[1].rate-limits[0].bandwidths[0].capacity}")
    private Integer linksRateLimit;
    @MockBean
    private UserService userService;
    @MockBean
    private LinkService linkService;

    @Test
    @SneakyThrows
    public void testRateLimitForTgChatEndpoint() {
        Long chatId = 123L;
        final String expectedResponseBody = "{ \"status\": 429, \"error\": \"Too Many Requests\", "
            + "\"message\": \"You have exhausted your API Request Quota for /tg-chat endpoint\" }";
        Mockito.doNothing().when(userService).addUser(new User(chatId, null));

        for (int i = 0; i < chatRateLimit; ++i) {
            log.info("DDoS attack to /tg-chat number: %d".formatted(i + 1));
            mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/%d".formatted(chatId))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        }
        log.info("DDoS attack to /tg-chat number: %d".formatted( chatRateLimit + 1));
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/%d".formatted(chatId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests())
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseBody));
        log.info("Unfortunately, blocked! Rate limit from your IP for /tg-chat had been ended!");
    }

    @Test
    @SneakyThrows
    public void testRateLimitForLinksEndpoint() {
        Long chatId = 123L;
        final String expectedResponseBody = "{ \"status\": 429, \"error\": \"Too Many Requests\", "
            + "\"message\": \"You have exhausted your API Request Quota for /links endpoint\" }";
        Mockito.doReturn(new ArrayList<Link>(List.of())).when(linkService).findAllLinksForUser(chatId);

        for (int i = 0; i < linksRateLimit; ++i) {
            log.info("DDoS attack to /links number: %d".formatted(i + 1));
            mockMvc.perform(MockMvcRequestBuilders.get("/links")
                    .header("Tg-Chat-Id", String.valueOf(chatId))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        log.info("DDoS attack to /links number: %d".formatted(linksRateLimit + 1));
        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests())
            .andExpect(MockMvcResultMatchers.content().json(expectedResponseBody));
        log.info("Unfortunately, blocked! Rate limit from your IP for /links had been ended!");
    }
}
