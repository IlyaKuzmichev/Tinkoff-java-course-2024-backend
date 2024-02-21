package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/list")
@Qualifier("command_list_add")
public final class ListCommand implements Command {
    private static final String NAME = "/list";
    private static final String DESCRIPTION = "Shows list of tracking links";
    private static final String NO_REGISTRATION = "Need to be registered for tracking links";
    private static final String NO_LINKS = "You have no links for tracking";
    private static final String FIRST_LINE = "Your tracking links:\n";
    private static final String FORMATTED_STRING = "%d. %s\n";
    UserRegistry userRegistry;

    public ListCommand(UserRegistry userRegistry) {
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
        StringBuilder builder = new StringBuilder();
        var user = userRegistry.getUser(update.message().chat().id());
        if (user.isEmpty()) {
            return NO_REGISTRATION;
        }
        if (user.get().getLinks().isEmpty()) {
            return NO_LINKS;
        }
        builder.append(FIRST_LINE);
        int counter = 1;
        for (var link : user.get().getLinks()) {
            builder.append(FORMATTED_STRING.formatted(counter++, link));
        }

        return builder.toString();
    }
}
