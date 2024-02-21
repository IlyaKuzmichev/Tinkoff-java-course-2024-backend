package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.service.ResponseService;
import java.util.List;

public class MyTelegramBot implements UpdatesListener {
    private final TelegramBot bot;
    private final ResponseService responseService;

    public MyTelegramBot(TelegramBot bot, ResponseService responseService) {
        this.bot = bot;
        this.responseService = responseService;
    }

    public void start() {
        bot.execute(createMenu());
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (var update : list) {
            if (update.message() != null) {
                bot.execute(new SendMessage(update.message().chat().id(), responseService.getAnswer(update)));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SetMyCommands createMenu() {
        var commands = responseService.getCommands();
        BotCommand[] botCommands = new BotCommand[commands.size()];
        for (var i = 0; i < commands.size(); ++i) {
            botCommands[i] = new BotCommand(commands.get(i).command(), commands.get(i).description());
        }
        return new SetMyCommands(botCommands);
    }
}
