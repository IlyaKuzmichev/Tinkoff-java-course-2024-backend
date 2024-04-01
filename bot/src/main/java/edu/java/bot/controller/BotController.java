package edu.java.bot.controller;

import edu.java.bot.MyTelegramBot;
import edu.java.bot.controller.dto.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotController {
    private final MyTelegramBot bot;

    @Autowired
    public BotController(MyTelegramBot bot) {
        this.bot = bot;
    }

    @PostMapping
    public void processUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        String message = linkUpdateRequest.description() + "\n" + linkUpdateRequest.url();
        for (Long chatId : linkUpdateRequest.tgChatIds()) {
            bot.sendMessage(chatId, message);
        }
    }
}
