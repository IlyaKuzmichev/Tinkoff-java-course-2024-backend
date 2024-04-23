package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.service.UpdateNotifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotController {
    private final UpdateNotifierService updateNotifierService;

    @Autowired
    public BotController(UpdateNotifierService updateNotifierService) {
        this.updateNotifierService = updateNotifierService;
    }

    @PostMapping
    public void processUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        updateNotifierService.notify(
            linkUpdateRequest.description(),
            linkUpdateRequest.url(),
            linkUpdateRequest.tgChatIds()
        );
    }
}
