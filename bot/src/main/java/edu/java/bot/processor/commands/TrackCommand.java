package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.database.UserState;
import java.util.Optional;

final class TrackCommand extends Command {
    private static final String NO_REGISTRATION = "You need to be registered for tracking links";
    private static final String LINK_INVITATION = "Input the link for tracking";

    TrackCommand(UserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Add new link for tracking";
    }

    @Override
    protected Optional<String> execute(Update update) {
        var userId = update.message().chat().id();
        if (userRegistry.getUser(userId).isEmpty()) {
            return Optional.of(NO_REGISTRATION);
        }
        userRegistry.getUser(userId).get().setState(UserState.WAIT_TRACK_URI);
        return Optional.of(LINK_INVITATION);
    }
}
