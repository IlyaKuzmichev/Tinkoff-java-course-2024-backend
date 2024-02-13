package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.User;
import edu.java.bot.database.UserRegistry;
import java.util.Optional;

public final class StartCommand extends Command {
    private static final String REGISTRATION_SUCCESS = "You were successfully registered";
    private static final String ALREADY_REGISTERED = "You're already registered";

    public StartCommand(UserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Command to start the bot";
    }

    @Override
    protected Optional<String> execute(Update update) {
        var userId = update.message().chat().id();
        if (userRegistry.getUser(userId).isEmpty()) {
            userRegistry.putUser(new User(userId));
            return Optional.of(REGISTRATION_SUCCESS);
        }
        return Optional.of(ALREADY_REGISTERED);
    }
}
