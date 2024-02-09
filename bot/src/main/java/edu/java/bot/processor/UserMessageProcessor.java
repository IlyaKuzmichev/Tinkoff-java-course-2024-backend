package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;

public interface UserMessageProcessor {
    String process(Update update);
}
