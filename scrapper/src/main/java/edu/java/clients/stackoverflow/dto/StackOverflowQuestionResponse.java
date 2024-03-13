package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;


public record StackOverflowQuestionResponse(
    @JsonProperty("question_id") int questionId,
    @JsonProperty("last_activity_date") long lastActivityDateUnix,
    @JsonProperty("answer_count") int answersCount
) {
    public OffsetDateTime getLastActivityDate() {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDateUnix), ZoneId.systemDefault());
    }

    public record StackOverflowQuestionResponseList(
        @JsonProperty("items") List<StackOverflowQuestionResponse> items
    ) {}


}
