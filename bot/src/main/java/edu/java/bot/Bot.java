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
        ApplicationConfig appConfig = new ApplicationConfig(System.getenv("TELEGRAM_API_KEY"));
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
            if (update.message() != null) {
                bot.execute(responseService.getAnswer(update));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SetMyCommands createMenu() {
        return new SetMyCommands();
    }
}
