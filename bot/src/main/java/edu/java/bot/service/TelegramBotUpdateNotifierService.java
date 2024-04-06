package edu.java.bot.service;

import edu.java.bot.MyTelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramBotUpdateNotifierService implements UpdateNotifierService {
    private final MyTelegramBot bot;

    @Override
    public void notify(String description, String url, Iterable<Long> chatIds) {
        String message = description + "\n" + url;
        for (Long chatId : chatIds) {
            bot.sendMessage(chatId, message);
        }
    }
}
