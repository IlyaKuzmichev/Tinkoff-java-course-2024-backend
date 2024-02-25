package edu.java.client;

import edu.java.model.StackOverflowQuestionResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<Optional<StackOverflowQuestionResponse>> fetchQuestion(int questionId);
}
