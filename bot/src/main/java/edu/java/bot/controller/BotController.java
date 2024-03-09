package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    @PostMapping("/updates")
    public void processUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        // Update processing logic
    }
}
