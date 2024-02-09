package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import java.util.Optional;

final class ListCommand extends Command {
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "Show list of tracking links";
    private static final String NO_REGISTRATION = "Need to be registered for tracking links";
    private static final String NO_LINKS = "You have no links for tracking";
    private static final String FIRST_LINE = "Your tracking links:\n";
    private static final String FORMATTED_STRING = "%d. %s\n";

    public ListCommand(UserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    protected Optional<String> execute(Update update) {
        StringBuilder builder = new StringBuilder();
        var user = userRegistry.getUser(update.message().chat().id());
        if (user.isEmpty()) {
            return Optional.of(NO_REGISTRATION);
        }
        if (user.get().getLinks().isEmpty()) {
            return Optional.of(NO_LINKS);
        }
        builder.append(FIRST_LINE);
        int counter = 1;
        for (var link : user.get().getLinks()) {
            builder.append(FORMATTED_STRING.formatted(counter++, link.toString()));
        }

        return Optional.of(builder.toString());
    }
}
