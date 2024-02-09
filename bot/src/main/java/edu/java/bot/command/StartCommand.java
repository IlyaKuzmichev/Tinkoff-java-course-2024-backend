package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.User;
import edu.java.bot.database.UserRegistry;
import java.util.Optional;

final class StartCommand extends Command {
    private static final String REGISTRATION_SUCCESS = "You were successfully registered";
    private static final String ALLREADY_REGISTERED = "You're allready registered";
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
        return Optional.of(ALLREADY_REGISTERED);
    }
}
