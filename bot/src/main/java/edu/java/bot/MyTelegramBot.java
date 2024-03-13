package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.processor.commands.Command;
import edu.java.bot.service.ResponseService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
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
            log.debug(update.message().text());
            log.debug(update.message().chat().toString());
            if (update.message() != null) {
                bot.execute(new SendMessage(update.message().chat().id(), responseService.getAnswer(update)));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendMessage(Long chatId, String message) {
        bot.execute(new SendMessage(chatId, message));
    }

    private SetMyCommands createMenu() {
        return new SetMyCommands(commandList.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new));
    }
}
