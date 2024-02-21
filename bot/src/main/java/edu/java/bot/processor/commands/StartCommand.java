package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.User;
import edu.java.bot.database.UserRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/start")
@Qualifier("command_list_add")
public final class StartCommand implements Command {
    private static final String NAME = "/start";
    private static final String DESCRIPTION = "Register new user";
    private static final String REGISTRATION_SUCCESS = "You were successfully registered";
    private static final String ALREADY_REGISTERED = "You're already registered";
    UserRegistry userRegistry;

    public StartCommand(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @Override
    public String commandName() {
        return NAME;
    }

    @Override
    public String commandDescription() {
        return DESCRIPTION;
    }

    @Override
    public String execute(Update update) {
        var userId = update.message().chat().id();
        if (userRegistry.getUser(userId).isEmpty()) {
            userRegistry.putUser(new User(userId));
            return REGISTRATION_SUCCESS;
        }
        return ALREADY_REGISTERED;
    }
}
