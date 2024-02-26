package edu.java.clients.stackoverflow;

import edu.java.model.StackOverflowQuestionResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<StackOverflowQuestionResponse> fetchQuestion(int questionId);
}
