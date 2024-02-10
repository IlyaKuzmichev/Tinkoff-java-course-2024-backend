package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.database.UserState;
import java.util.Optional;

final class UntrackCommand extends Command {
    private static final String NO_REGISTRATION = "You need to be registered for tracking links";
    private static final String EMPTY_LINK_LIST = "You have no tracking links";
    private static final String LINK_INVITATION = "Input link for untracking";

    UntrackCommand(UserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Untrack one of your links";
    }

    @Override
    protected Optional<String> execute(Update update) {
        var userId = update.message().chat().id();
        if (userRegistry.getUser(userId).isEmpty()) {
            return Optional.of(NO_REGISTRATION);
        }
        if (userRegistry.getUser(userId).get().getLinks().isEmpty()) {
            return Optional.of(EMPTY_LINK_LIST);
        }
        userRegistry.getUser(userId).get().setState(UserState.WAIT_UNTRACK_URI);
        return Optional.of(LINK_INVITATION);
    }
}
