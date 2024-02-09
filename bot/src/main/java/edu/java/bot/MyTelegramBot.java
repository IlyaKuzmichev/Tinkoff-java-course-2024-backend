package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.ResponseService;

public class MyTelegramBot {
    private final TelegramBot bot;
    private final ResponseService responseService;

    public MyTelegramBot() {
        ApplicationConfig appConfig = new ApplicationConfig(System.getenv("TELEGRAM_API_KEY"));
        bot = new TelegramBot(appConfig.telegramToken());
        responseService = new ResponseService();
        setListener();
        bot.execute(createMenu());
    }

    private void setListener() {
        bot.setUpdatesListener(updates -> {
            for (var update : updates) {
                bot.execute(responseService.getAnswer(update));
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                e.printStackTrace();
            }
        });
    }

    private SetMyCommands createMenu() {
        return new SetMyCommands();
    }
}
