package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class StackOverflowQuestionResponse {
    private String title;
    private String link;
    @JsonProperty("question_id")
    private int questionId;
    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public OffsetDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(OffsetDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }
}
