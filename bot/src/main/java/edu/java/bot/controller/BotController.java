package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotController {
    @PostMapping
    public void processUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        // Update processing logic
    }
}
