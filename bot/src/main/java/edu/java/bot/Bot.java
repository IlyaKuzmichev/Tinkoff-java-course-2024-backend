package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.ResponseService;
import java.util.List;

public class Bot implements UpdatesListener {
    private final TelegramBot bot;
    private final ResponseService responseService;

    public Bot() {
        ApplicationConfig appConfig = new ApplicationConfig("6719599434:AAG7db_6bif093UbW6gP7jSdc2vVWiVdwvk");
        bot = new TelegramBot(appConfig.telegramToken());
        responseService = new ResponseService();
    }

    public void start() {
        bot.execute(createMenu());
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (var update : list) {
            bot.execute(responseService.getAnswer(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SetMyCommands createMenu() {
        return new SetMyCommands();
    }
}
