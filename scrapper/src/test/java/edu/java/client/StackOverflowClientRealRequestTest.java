package edu.java.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:test", properties = "client.stackoverflow.base-url=https://api.stackexchange.com/2.3")
@EnabledIfEnvironmentVariable(named = "SCRAPPER_REAL_TESTS_ENABLE", matches = "1")
public class StackOverflowClientRealRequestTest {
    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Test
    public void testFetchQuestionStackOverflowClient() {
        int questionId = 11227809;

        var optResponse = stackOverflowClient.fetchQuestion(questionId).block();

        assertNotNull(optResponse);
        assertTrue(optResponse.isPresent());

        var response = optResponse.get();

        assertEquals(response.questionId(), questionId);
        assertNotNull(response.getLastActivityDate());
    }

    @Test
    public void testFetchQuestionNoItems() {
        int questionId = 1;

        var optResponse = stackOverflowClient.fetchQuestion(questionId).block();

        assertNotNull(optResponse);
        assertTrue(optResponse.isEmpty());
    }
}
