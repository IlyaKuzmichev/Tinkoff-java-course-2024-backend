package edu.java.controller.dto;

import edu.java.models.User;

public record SetStatusRequest(User.Status status) {
}
