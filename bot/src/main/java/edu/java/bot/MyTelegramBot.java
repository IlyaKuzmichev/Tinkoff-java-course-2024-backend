package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.processor.commands.Command;
import edu.java.bot.service.ResponseService;
import java.util.List;

public class MyTelegramBot implements UpdatesListener {
    private final TelegramBot bot;
    private final ResponseService responseService;
    private final List<Command> commandList;

    public MyTelegramBot(TelegramBot bot, List<Command> commandList, ResponseService responseService) {
        this.bot = bot;
        this.commandList = commandList;
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
        return new SetMyCommands(commandList.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new));
    }
}
