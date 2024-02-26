package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.database.UserState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/track")
@Qualifier("command_list_add")
public final class TrackCommand implements Command {
    private static final String NAME = "/track";
    private static final String DESCRIPTION = "Add new link for tracking";
    private static final String NO_REGISTRATION = "You need to be registered for tracking links";
    private static final String LINK_INVITATION = "Input the link for tracking";
    UserRegistry userRegistry;

    public TrackCommand(UserRegistry userRegistry) {
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
            return NO_REGISTRATION;
        }
        userRegistry.getUser(userId).get().setState(UserState.WAIT_TRACK_URI);
        return LINK_INVITATION;
    }
}

