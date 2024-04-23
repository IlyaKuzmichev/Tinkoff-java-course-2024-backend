package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.controller.dto.LinkUpdateRequest;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test")
public class RateLimiterBotControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}")
    private Integer rateLimit;

    @Test
    @SneakyThrows
    public void testRateLimitWorksForBotController() {
        LinkUpdateRequest
            linkUpdateRequest = new LinkUpdateRequest(1L, "https://example.com", "description", List.of(1L, 2L, 3L));
        String requestBody = objectMapper.writeValueAsString(linkUpdateRequest);

        for (int i = 0; i < rateLimit; ++i) {
            mockMvc.perform(MockMvcRequestBuilders.post("/updates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        for (int i = 0; i < 5; ++i) {
            mockMvc.perform(MockMvcRequestBuilders.post("/updates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isTooManyRequests());

        }
    }
}
