package edu.java.clients.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubPullRequestsResponse(
   @JsonProperty("total_count") Integer pullRequestsCount
) {}
