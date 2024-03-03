package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.StackOverflowQuestionResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<Optional<StackOverflowQuestionResponse>> fetchQuestion(int questionId);
}
