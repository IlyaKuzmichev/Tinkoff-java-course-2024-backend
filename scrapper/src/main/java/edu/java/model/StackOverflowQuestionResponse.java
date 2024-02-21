package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public record StackOverflowQuestionResponse(
    @JsonProperty("items") List<StackOverflowItemResponse> items
) {
    public record StackOverflowItemResponse(
        @JsonProperty("question_id") int questionId,
        @JsonProperty("last_activity_date") long lastActivityDateUnix
    ) {
        public OffsetDateTime getLastActivityDate() {
            return OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDateUnix), ZoneId.systemDefault());
        }
    }
}