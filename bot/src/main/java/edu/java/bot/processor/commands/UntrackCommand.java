package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.database.UserState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/untrack")
@Qualifier("action_command")
public final class UntrackCommand implements Command {
    private static final String NAME = "/untrack";
    private static final String DESCRIPTION = "Untrack one of your links";
    private static final String NO_REGISTRATION = "You need to be registered for tracking links";
    private static final String EMPTY_LINK_LIST = "You have no tracking links";
    private static final String LINK_INVITATION = "Input link for untracking";
    UserRegistry userRegistry;

    public UntrackCommand(UserRegistry userRegistry) {
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
        var user = userRegistry.getUser(update.message().chat().id());
        if (user.isEmpty()) {
            return NO_REGISTRATION;
        }
        if (user.get().getLinks().isEmpty()) {
            return EMPTY_LINK_LIST;
        }
        user.get().setState(UserState.WAIT_UNTRACK_URI);
        return LINK_INVITATION;
    }
}

