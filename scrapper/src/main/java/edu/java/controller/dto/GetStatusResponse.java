package edu.java.controller.dto;

import edu.java.models.User;

public record GetStatusResponse(User.Status status) {
}
